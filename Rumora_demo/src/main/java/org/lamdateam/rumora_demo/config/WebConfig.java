package org.lamdateam.rumora_demo.config;

import org.springframework.context.annotation.Configuration;
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
}