-- Roles
INSERT INTO roles (name) VALUES ('CLIENT'), ('SELLER'), ('ADMIN')
ON CONFLICT (name) DO NOTHING;

-- Categorías de ejemplo
INSERT INTO categories (name) VALUES
  ('Alimentos'), ('Tecnología'), ('Ropa'), ('Servicios')
ON CONFLICT (name) DO NOTHING;

-- Productos dummy
INSERT INTO products (name, description, price, stock, category_id)
SELECT 'Playera UAM', 'Playera oficial con logo', 199.00, 25, c.id
FROM categories c WHERE c.name='Ropa'
ON CONFLICT DO NOTHING;

INSERT INTO products (name, description, price, stock, category_id)
SELECT 'Cable USB-C', '1m, carga rápida', 99.00, 50, c.id
FROM categories c WHERE c.name='Tecnología'
ON CONFLICT DO NOTHING;
