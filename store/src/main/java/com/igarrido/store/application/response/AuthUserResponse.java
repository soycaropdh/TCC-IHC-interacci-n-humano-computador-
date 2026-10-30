package com.igarrido.store.application.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthUserResponse {
    private String token;
    private String username;
    private String role;
}