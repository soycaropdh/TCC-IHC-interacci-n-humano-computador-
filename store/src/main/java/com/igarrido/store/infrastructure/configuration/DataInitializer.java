package com.igarrido.store.infrastructure.configuration;

import com.igarrido.store.domain.constants.ProductCategory;
import com.igarrido.store.domain.model.Product;
import com.igarrido.store.domain.model.User;
import com.igarrido.store.domain.repository.ProductRepository;
import com.igarrido.store.domain.repository.UserRepository;
import com.igarrido.store.infrastructure.persistence.entity.MaterialEntity;
import com.igarrido.store.infrastructure.persistence.entity.ProductEntity;
import com.igarrido.store.infrastructure.persistence.jpa.ProductJpaRepository;
import com.igarrido.store.infrastructure.persistence.jpa.MaterialJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ProductJpaRepository productJpaRepository;
    private final MaterialJpaRepository materialRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        createUserIfMissing("admin", "admin123", "ADMIN");
        createUserIfMissing("admin2", "admin123", "ADMIN");
        createUserIfMissing("shop", "shop123", "SHOP");
        replaceLegacyCatalogIfNeeded();
        seedDeliProductsIfEmpty();
        seedMaterialsIfEmpty();
    }

    private void createUserIfMissing(String username, String rawPassword, String role) {
        if (userRepository.findByUsername(username).isEmpty()) {
            userRepository.save(User.builder().username(username).password(passwordEncoder.encode(rawPassword)).role(role).build());
            System.out.println("Usuario " + role + " creado: " + username + " / " + rawPassword);
        }
    }


    private void replaceLegacyCatalogIfNeeded() {
        Set<String> valid = Set.of("QUESO", "BOCADILLO", "MIXTO", "JAMON");
        boolean legacy = productJpaRepository.findAll().stream()
                .map(ProductEntity::getCategory)
                .filter(c -> c != null && !c.isBlank())
                .anyMatch(c -> !valid.contains(c));
        if (legacy) {
            productJpaRepository.deleteAllInBatch();
            System.out.println("Catálogo anterior de electrodomésticos eliminado. Se cargará DeliDeditos by Caro.");
        }
    }

    private void seedDeliProductsIfEmpty() {
        // Solo carga los datos iniciales una vez. Los cambios hechos desde la interfaz
        // no deben perderse al reiniciar el backend.
        if (productJpaRepository.count() > 0) {
            return;
        }
        List<Product> products = List.of(
                deli("Deditos de queso", ProductCategory.QUESO, "12 deditos rellenos de queso.", "10000", 20),
                deli("Deditos de bocadillo", ProductCategory.BOCADILLO, "12 deditos rellenos de bocadillo.", "10000", 20),
                deli("6 bocadillo + 6 queso", ProductCategory.MIXTO, "Combo de 12 deditos: 6 de bocadillo y 6 de queso.", "10000", 20),
                deli("Deditos de bocadillo con queso", ProductCategory.MIXTO, "12 deditos rellenos de bocadillo con queso.", "12000", 20),
                deli("Deditos de jamón", ProductCategory.JAMON, "12 deditos rellenos de jamón. Precio provisional: $10.000.", "10000", 20),
                deli("Deditos de jamón con queso", ProductCategory.JAMON, "12 deditos rellenos de jamón con queso.", "12000", 20)
        );
        products.forEach(productRepository::save);
    }

    private Product deli(String name, ProductCategory category, String description, String price, int stock) {
        return Product.builder().name(name).brand("DeliDeditos by Caro").category(category).description(description)
                .price(new BigDecimal(price)).stock(stock).updatedAt(LocalDateTime.now()).build();
    }

    private void seedMaterialsIfEmpty() {
        // Solo carga los materiales iniciales si la tabla está vacía. Así los precios,
        // cantidades, mínimos y consumos configurados por la usuaria se conservan.
        if (materialRepository.count() > 0) {
            return;
        }
        List<MaterialEntity> materials = List.of(
                material("Harina", "libras", "5", "10500"),
                material("Azúcar", "libras", "2", "3400"),
                material("Sal", "libra", "1", "2000"),
                material("Vitina", "libra", "1", null),
                material("Extracto de mantequilla", "por definir", "0", null),
                material("Polvo de hornear", "g", "250", "3000"),
                material("Leche en polvo", "libra", "1", null),
                material("Queso", "libras", "2", null),
                material("Bocadillo", "por definir", "0", null),
                material("Jamón", "por definir", "0", null),
                material("Queso mozzarella", "por definir", "0", null)
        );
        materials.forEach(materialRepository::save);
    }

    private MaterialEntity material(String name, String unit, String stock, String price) {
        return MaterialEntity.builder().name(name).unit(unit).stock(new BigDecimal(stock))
                .price(price == null ? null : new BigDecimal(price))
                .batchConsumption(BigDecimal.ZERO).minStock(BigDecimal.ZERO).build();
    }
}
