-- Crear tabla products
CREATE TABLE products (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price NUMERIC(10,2) NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Crear tabla price_history
CREATE TABLE price_history (
    id SERIAL PRIMARY KEY,
    product_id INT NOT NULL,
    price NUMERIC(10,2) NOT NULL,
    changed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_product FOREIGN KEY (product_id)
        REFERENCES products (id)
        ON DELETE CASCADE
);
