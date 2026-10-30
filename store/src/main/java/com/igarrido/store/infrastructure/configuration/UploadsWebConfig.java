package com.igarrido.store.infrastructure.configuration;

import com.igarrido.store.infrastructure.storage.LocalImageStorage;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Publica la carpeta de imágenes en la dirección /uploads/**
 * (por ejemplo http://localhost:8081/uploads/abc123.png).
 */
@Configuration
public class UploadsWebConfig implements WebMvcConfigurer {

    private final LocalImageStorage storage;

    public UploadsWebConfig(LocalImageStorage storage) {
        this.storage = storage;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String location = storage.getRoot().toUri().toString();
        if (!location.endsWith("/")) {
            location = location + "/";
        }
        registry.addResourceHandler("/uploads/**").addResourceLocations(location);
    }
}
