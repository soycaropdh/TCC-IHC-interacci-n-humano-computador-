package com.igarrido.store.application.websocket;

import com.igarrido.store.infrastructure.locks.ProductLockService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ProductWsController {
    private final ProductLockService lockService;
    private final SimpMessagingTemplate messaging;

    @MessageMapping("/product/lock")
    public void lockProduct(@Payload Map<String, Object> body, Principal principal) {
        Long productId = Long.valueOf(body.get("productId").toString());
        String username = principal.getName();

        boolean locked = lockService.tryLock(productId, username);
        messaging.convertAndSend("/topic/product/" + productId + "/lock",
                Map.of("productId", productId,
                        "locked", locked,
                        "owner", lockService.getOwner(productId))
        );

        messaging.convertAndSend("/topic/products/locks",
                Map.of("locks", lockService.locks.keySet().stream().toList())
        );
    }

    @MessageMapping("/product/unlock")
    public void unlockProduct(@Payload Map<String, Object> body, Principal principal) {
        Long productId = Long.valueOf(body.get("productId").toString());
        String username = principal.getName();

        lockService.release(productId, username);
        messaging.convertAndSend("/topic/products/locks",
                Map.of("locks", lockService.locks.keySet().stream().toList())
        );
    }

    private void checkAdmin(Authentication auth) {
        if (auth == null || auth.getAuthorities().stream().noneMatch(
                a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            throw new RuntimeException("Only ADMIN can perform this action");
        }
    }
}
