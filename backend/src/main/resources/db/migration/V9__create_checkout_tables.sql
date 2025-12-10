-- 1. Tabla de Ubicaciones (Locations)
CREATE TABLE locations (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    is_default BOOLEAN DEFAULT FALSE,
    CONSTRAINT fk_locations_user FOREIGN KEY (user_id) REFERENCES users(id)
);

-- 2. Tabla de Órdenes (Orders)
CREATE TABLE orders (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(50) NOT NULL, -- PENDING, PAID, DELIVERED
    total NUMERIC(12, 2) NOT NULL DEFAULT 0.00,
    payment_method VARCHAR(50),
    location_id BIGINT,
    CONSTRAINT fk_orders_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_orders_location FOREIGN KEY (location_id) REFERENCES locations(id)
);

-- 3. Tabla de Items de Orden (OrderItems)
CREATE TABLE order_items (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INTEGER NOT NULL,
    price NUMERIC(12, 2) NOT NULL, -- Precio snapshot al momento de compra
    CONSTRAINT fk_order_items_order FOREIGN KEY (order_id) REFERENCES orders(id),
    CONSTRAINT fk_order_items_product FOREIGN KEY (product_id) REFERENCES products(id)
);