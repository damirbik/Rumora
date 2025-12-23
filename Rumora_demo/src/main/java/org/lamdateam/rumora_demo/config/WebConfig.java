package org.lamdateam.rumora_demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Раздача обложек: /uploads/covers/... → uploads/covers/
        registry.addResourceHandler("/uploads/covers/**")
                .addResourceLocations("file:uploads/covers/");

        // Раздача аудио: /uploads/audio/... → uploads/audio/
        registry.addResourceHandler("/uploads/audio/**")
                .addResourceLocations("file:uploads/audio/");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:3000", "http://127.0.0.1:3000", "remjest-rumora-5a51.twc1.net")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}