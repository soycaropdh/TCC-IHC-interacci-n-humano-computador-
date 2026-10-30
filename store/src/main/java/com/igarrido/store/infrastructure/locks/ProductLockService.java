package com.igarrido.store.infrastructure.locks;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ProductLockService {
    @Value("${product.ttl-minutes:5}")
    private int minutes;

    public final Map<Long, LockInfo> locks = new ConcurrentHashMap<>();
    private final long ttlMillis = minutes * 60 * 1000; // 5 min

    public record LockInfo(String owner, Instant expiresAt) {}

    public synchronized boolean tryLock(Long productId, String username) {
        LockInfo current = locks.get(productId);
        if (current == null || current.expiresAt().isBefore(Instant.now())) {
            locks.put(productId, new LockInfo(username, Instant.now().plusMillis(ttlMillis)));
            return false;
        }
        return !current.owner().equals(username);
    }

    public synchronized void release(Long productId, String username) {
        LockInfo current = locks.get(productId);
        if (current != null && current.owner().equals(username)) {
            locks.remove(productId);
        }
    }

    public String getOwner(Long productId) {
        LockInfo current = locks.get(productId);
        return current == null ? null : current.owner();
    }

    public boolean isLocked(Long productId) {
        LockInfo current = locks.get(productId);
        return current != null && current.expiresAt().isAfter(Instant.now());
    }

    public List<Long> releaseAllOwnedBy(String username) {
        List<Long> released = new ArrayList<>();
        locks.forEach((productId, owner) -> {
            if (owner.equals(username)) {
                locks.remove(productId);
                released.add(productId);
            }
        });
        return released;
    }
}
