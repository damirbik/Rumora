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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // ===== ОТЛАДКА: логируем каждый запрос =====
        System.out.println("[DEBUG] JwtAuthenticationFilter invoked");
        String authHeader = request.getHeader("Authorization");
        System.out.println("[DEBUG] Authorization header: " + authHeader);

        // Пропускаем, если нет заголовка или неверный формат
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("[DEBUG] No valid Authorization header — skipping JWT");
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);
        System.out.println("[DEBUG] Token (first 30 chars): " + token.substring(0, Math.min(30, token.length())) + "...");

        // Проверяем валидность токена
        if (!tokenProvider.validateToken(token)) {
            System.out.println("[DEBUG] Invalid or expired token — skipping authentication");
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // Извлекаем username и роль из токена
            String userIdStr = tokenProvider.getUsernameFromToken(token); // ← Это теперь user_id
            Long userId = Long.parseLong(userIdStr);
            String role = tokenProvider.getRoleFromToken(token);
            System.out.println("[DEBUG] Extracted username: " + userIdStr + ", role: " + role);

            // Загружаем пользователя
            UserDetails userDetails = customUserDetailsService.loadUserById(userId);
            System.out.println("[DEBUG] User loaded successfully: " + userDetails.getUsername());

            // Создаём authorities
            GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role);
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails, null, Collections.singletonList(authority)
            );

            // Сохраняем аутентификацию
            SecurityContextHolder.getContext().setAuthentication(authToken);
            System.out.println("[DEBUG] Authentication set in SecurityContext");

        } catch (UsernameNotFoundException e) {
            System.err.println("[ERROR] User not found during JWT auth: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("[ERROR] Unexpected error in JWT filter: " + e.getMessage());
            e.printStackTrace(System.err);
        }

        filterChain.doFilter(request, response);
    }
}