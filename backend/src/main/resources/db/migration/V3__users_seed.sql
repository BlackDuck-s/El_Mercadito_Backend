-- Usuarios base (password = 123456, BCRYPT)
-- Generado con cost 10: $2b$10$kFWZDgT2ZGm8cMkQg5OxSe5JUhk.U2Z/AkcVKRHEm3DGOavjJVOpm
INSERT INTO users (email, password, enabled)
VALUES 
 ('seller@uam.mx', '$2b$10$kFWZDgT2ZGm8cMkQg5OxSe5JUhk.U2Z/AkcVKRHEm3DGOavjJVOpm', true),
 ('admin@uam.mx',  '$2b$10$kFWZDgT2ZGm8cMkQg5OxSe5JUhk.U2Z/AkcVKRHEm3DGOavjJVOpm', true)
ON CONFLICT (email) DO NOTHING;

-- Asegura roles existentes (por si V2 no corrió antes)
INSERT INTO roles(name) VALUES ('CLIENT') ON CONFLICT(name) DO NOTHING;
INSERT INTO roles(name) VALUES ('SELLER') ON CONFLICT(name) DO NOTHING;
INSERT INTO roles(name) VALUES ('ADMIN')  ON CONFLICT(name) DO NOTHING;

-- Asigna SELLER al seller@uam.mx
INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u, roles r
WHERE u.email = 'seller@uam.mx' AND r.name = 'SELLER'
ON CONFLICT (user_id, role_id) DO NOTHING;

-- Asigna ADMIN al admin@uam.mx
INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u, roles r
WHERE u.email = 'admin@uam.mx' AND r.name = 'ADMIN'
ON CONFLICT (user_id, role_id) DO NOTHING;
