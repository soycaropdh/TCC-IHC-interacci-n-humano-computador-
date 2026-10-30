package com.igarrido.store.application.rest;

import com.igarrido.store.application.response.UserSummaryResponse;
import com.igarrido.store.domain.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping
    public List<UserSummaryResponse> all() {
        return service.findAll().stream()
                .map(u -> new UserSummaryResponse(u.getId(), u.getUsername(), u.getRole()))
                .toList();
    }
}
