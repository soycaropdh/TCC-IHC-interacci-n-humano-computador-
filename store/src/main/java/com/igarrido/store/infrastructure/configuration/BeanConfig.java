package com.igarrido.store.infrastructure.configuration;

import com.igarrido.store.domain.port.ImageStoragePort;
import com.igarrido.store.domain.port.PasswordHasher;
import com.igarrido.store.domain.repository.UserRepository;
import com.igarrido.store.domain.service.ProductImageService;
import com.igarrido.store.domain.service.ProductService;
import com.igarrido.store.domain.service.UserService;
import com.igarrido.store.domain.repository.ProductRepository;
import com.igarrido.store.domain.service.WebSocketCommunicationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@Configuration
public class BeanConfig {
    @Bean
    public ProductService productService(ProductRepository productRepository, WebSocketCommunicationService wsService) {
        return new ProductService(productRepository, wsService);
    }

    @Bean
    public ProductImageService productImageService(ImageStoragePort imageStoragePort) {
        return new ProductImageService(imageStoragePort);
    }

    @Bean
    public UserService userService(UserRepository userRepository, PasswordHasher passwordHasher) {
        return new UserService(userRepository, passwordHasher);
    }

    @Bean
    public WebSocketCommunicationService webSocketCommunicationService(SimpMessagingTemplate messaging){
        return new WebSocketCommunicationService(messaging);
    }
}
