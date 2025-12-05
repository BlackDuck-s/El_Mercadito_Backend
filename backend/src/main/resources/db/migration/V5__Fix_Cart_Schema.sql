ALTER TABLE shopping_carts
    ADD COLUMN IF NOT EXISTS status VARCHAR(50) NOT NULL DEFAULT 'PENDING';

ALTER TABLE shopping_carts
    DROP CONSTRAINT IF EXISTS uk_shopping_carts_user_id;
