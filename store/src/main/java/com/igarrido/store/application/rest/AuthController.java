package com.igarrido.store.application.rest;

import com.igarrido.store.application.request.AuthRequest;
import com.igarrido.store.application.request.RegisterRequest;
import com.igarrido.store.application.response.AuthUserResponse;
import com.igarrido.store.domain.model.User;
import com.igarrido.store.domain.service.UserService;
import com.igarrido.store.infrastructure.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<AuthUserResponse> login(@RequestBody AuthRequest request) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        return ResponseEntity.ok(buildResponse(auth));
    }

    /**
     * Crea una cuenta de cliente (rol SHOP) e inicia sesión automáticamente.
     */
    @PostMapping("/register")
    public ResponseEntity<AuthUserResponse> register(@RequestBody RegisterRequest request) {
        User created = userService.register(request.getUsername(), request.getPassword());

        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(created.getUsername(), request.getPassword())
        );

        return new ResponseEntity<>(buildResponse(auth), HttpStatus.CREATED);
    }

    private AuthUserResponse buildResponse(Authentication auth) {
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String jwt = jwtService.generateToken(userDetails);

        String role = userDetails.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElse("SHOP");

        return new AuthUserResponse(jwt, userDetails.getUsername(), role);
    }
}
