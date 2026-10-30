package com.igarrido.store.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Usuario del dominio. Ya no tiene anotaciones JPA: la tabla se describe
 * en infrastructure/persistence/entity/UserEntity.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    private Long id;
    private String username;
    private String password;
    private String role;
}
