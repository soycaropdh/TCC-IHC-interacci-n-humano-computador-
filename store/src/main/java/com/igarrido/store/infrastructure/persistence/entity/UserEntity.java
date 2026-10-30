package com.igarrido.store.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Tabla "users". Misma tabla y columnas que antes, así que tus usuarios
 * existentes (admin, admin2, shop) siguen funcionando.
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    private String role;
}
