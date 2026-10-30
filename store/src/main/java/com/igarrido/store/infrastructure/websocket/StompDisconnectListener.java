package com.igarrido.store.infrastructure.websocket;

import com.igarrido.store.infrastructure.locks.ProductLockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.security.Principal;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class StompDisconnectListener {

    private final ProductLockService lockService;
    private final SimpMessagingTemplate messaging;

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        Principal principal = accessor.getUser();

        if (principal == null) {
            log.warn("Desconexión sin usuario asociado");
            return;
        }

        String username = principal.getName();
        log.info("Usuario desconectado: {}", username);

        List<Long> released = lockService.releaseAllOwnedBy(username);

        for (Long productId : released) {
            messaging.convertAndSend("/topic/product/" + productId + "/lock",
                    java.util.Map.of(
                            "productId", productId,
                            "locked", false,
                            "owner", null
                    ));
        }
    }
}

