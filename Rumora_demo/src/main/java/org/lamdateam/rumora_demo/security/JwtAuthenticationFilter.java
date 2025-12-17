package org.lamdateam.rumora_demo.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        // Если заголовка нет или не начинается с "Bearer ", пропускаем
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7); // Убираем "Bearer "

        // Проверяем валидность токена
        if (!tokenProvider.validateToken(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        // Извлекаем username и роль из токена
        String username = tokenProvider.getUsernameFromToken(token);
        String role = tokenProvider.getRoleFromToken(token); // ← Добавим этот метод в JwtTokenProvider

        // Загружаем UserDetails (пользователя)
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // Создаём authorities: "ROLE_Admin", "ROLE_User" и т.д.
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role);
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userDetails, null, Collections.singletonList(authority)
        );

        // Устанавливаем аутентификацию в SecurityContext
        SecurityContextHolder.getContext().setAuthentication(authToken);

        // Продолжаем цепочку фильтров
        filterChain.doFilter(request, response);
    }
}