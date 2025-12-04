-- 1. Crear la tabla del carrito (padre)
-- Asumo que el carrito pertenece a un usuario, ajusta si es diferente en tu entidad ShoppingCart
CREATE TABLE shopping_carts (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT, 
    total NUMERIC(12, 2) DEFAULT 0.00,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    -- Si tienes relacion con Users descomenta la siguiente linea:
    -- CONSTRAINT fk_cart_user FOREIGN KEY (user_id) REFERENCES users(id)
);

-- 2. Crear la tabla de items del carrito (la que te falta)
CREATE TABLE cart_items (
    id BIGSERIAL PRIMARY KEY,
    cart_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INTEGER NOT NULL,
    unit_price NUMERIC(12, 2) NOT NULL,
    sub_total NUMERIC(12, 2) NOT NULL,
    
    CONSTRAINT fk_cart_items_cart FOREIGN KEY (cart_id) REFERENCES shopping_carts(id),
    CONSTRAINT fk_cart_items_product FOREIGN KEY (product_id) REFERENCES products(id)
);