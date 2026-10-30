package com.igarrido.store.domain.service;

import com.igarrido.store.domain.constants.ProductTypeChange;
import com.igarrido.store.domain.model.Product;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.Map;

public class WebSocketCommunicationService {

    private final SimpMessagingTemplate messaging;

    public WebSocketCommunicationService(SimpMessagingTemplate messaging) {
        this.messaging = messaging;
    }

    public void nofifyChanges(ProductTypeChange typeChange, Product product) {
        messaging.convertAndSend("/topic/products/refresh",
                Map.of(
                        "type", typeChange,
                        "data", product
                )
        );
    }
}
