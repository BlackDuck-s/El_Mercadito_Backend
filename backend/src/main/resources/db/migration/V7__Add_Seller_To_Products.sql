ALTER TABLE products ADD COLUMN seller_id BIGINT;

DELETE FROM products WHERE seller_id IS NULL;

ALTER TABLE products ALTER COLUMN seller_id SET NOT NULL;

ALTER TABLE products 
ADD CONSTRAINT fk_products_seller 
FOREIGN KEY (seller_id) REFERENCES users(id);