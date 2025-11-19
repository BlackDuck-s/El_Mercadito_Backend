# Gestión de equipo y asignación de tareas  
## Proyecto: El Mercadito UAM Cuajimalpa

Este documento describe la organización del equipo y la asignación de tareas durante la **fase de Elaboración**, siguiendo el **Proceso Unificado**.

El backend ya cuenta con:

- ✅ Autenticación con JWT
- ✅ Usuarios y roles reales (CLIENT, SELLER, ADMIN) cargados desde BD.
- ✅ CRUD de productos con control de acceso por rol (SELLER/ADMIN).
- ✅ Búsqueda y paginación de productos.
- ✅ Pruebas manuales básicas (Postman).
- ✅ Fases E3, E4 y E5 realizadas por el Backend Lead (Yayo).

A partir de este punto, se distribuye el trabajo por integrante.

---

## Roles principales

- **Yayo (Backend Lead)**  
  Responsable de la arquitectura del backend, seguridad, estándares y revisión técnica de PRs.

- **Max (Frontend Lead)**  
  Responsable de la interfaz web (React + Tailwind), experiencia de usuario y consumo de la API.

- **Edgar (Backend Dominio)**  
  Responsable de las funcionalidades de negocio: órdenes, carrito, reseñas y reglas de negocio asociadas.

- **Axel (QA & Testing)**  
  Responsable de pruebas manuales y automatizadas, calidad funcional y documentación de casos de prueba.

- **Emilio (DevOps & Entornos)**  
  Responsable de entornos, contenedores, pipelines de integración continua y despliegue.

---

## Asignación de tareas por integrante

### 🧑‍🎨 Max – Frontend (React/Tailwind)

**Iteración 1 – Autenticación + catálogo**

- Pantallas de **Login** y **Registro**:
  - Consumir `/auth/login` y `/auth/register`.
  - Guardar el `accessToken` y manejar errores básicos.
- **Rutas protegidas**:
  - Rutas públicas (home, catálogo) vs rutas protegidas (panel vendedor, perfil).
- **Catálogo**:
  - Vista de categorías y productos con paginación y filtros por texto/categoría.

**Iteración 2 – Panel de vendedor + detalle**

- Vista de **detalle de producto** (`/products/:id`).
- **Panel de vendedor**:
  - Formularios para crear, editar y eliminar productos.
  - Consumo de `POST/PUT/DELETE /products` usando token de SELLER/ADMIN.

> **DoD Max:**  
> El usuario puede iniciar sesión desde la UI, ver el catálogo y, con un usuario vendedor, crear/editar/borrar productos desde el frontend.

---

### 🧑‍💻 Edgar – Backend de dominio (órdenes, reseñas, carrito)

**Iteración 1 – Órdenes**

- Definir modelo y migraciones de:
  - `orders`
  - `order_items`
- Crear endpoints:
  - `POST /orders` (checkout desde un carrito).
  - `GET /orders/mine` (pedidos del cliente logueado).
  - `GET /orders/seller` (pedidos relacionados con productos del vendedor).

**Iteración 2 – Reseñas (reviews)**

- Modelo y migración para `reviews`.
- Endpoints:
  - `POST /products/{id}/reviews` (solo si el usuario compró ese producto).
  - `GET /products/{id}/reviews`.
- Calcular y exponer promedio de rating en el DTO de producto.

> **DoD Edgar:**  
> Un usuario puede crear pedidos reales (checkout) y ver sus órdenes; un comprador puede dejar reseñas y verlas asociadas a los productos comprados.

---

### 🧪 Axel – QA, pruebas e inspección

**Iteración 1 – Pruebas manuales**

- Crear/actualizar la **colección Postman** con:
  - Auth (`/auth/login`, `/auth/me`, `/auth/register`).
  - Catálogo (`/categories`, `/products`).
  - Productos (CRUD completo).
  - Órdenes y reseñas cuando estén listas.
- Diseñar una **matriz de casos de prueba** (Excel/Markdown):
  - Casos por flujo: login, registro, CRUD productos, checkout, reseñas, etc.

**Iteración 2 – Pruebas automatizadas**

- Implementar **tests de integración** con JUnit/SpringBootTest para:
  - Auth.
  - Productos.
  - Órdenes.
- Crear un **reporte de QA** (`QA_REPORT.md`) con bugs encontrados y estado.

> **DoD Axel:**  
> Cualquier integrante puede ejecutar la colección Postman y los tests automáticos y verificar los flujos principales sin intervención adicional.

---

### 🛠️ Emilio – DevOps & entornos

**Iteración 1 – Infraestructura local**

- Dockerizar el backend (`Dockerfile`) y consolidar `docker-compose.yml`:
  - Servicios: `backend`, `db` (y opcionalmente `frontend`).
- Organizar los perfiles de Spring:
  - `application-test.yml` (H2).
  - `application-dev.yml` (Postgres local).
  - (Opcional) `application-prod.yml`.
- Gestionar variables sensibles:
  - `JWT_SECRET`, credenciales DB, etc.

**Iteración 2 – CI/CD básico**

- Configurar un workflow de **GitHub Actions**:
  - Checkout del proyecto.
  - Cache de Maven.
  - Ejecución de `mvn test`.
- Crear `DEPLOYMENT.md`:
  - Cómo clonar el repo.
  - Cómo configurar las variables.
  - Cómo levantar todo con `docker compose up`.

> **DoD Emilio:**  
> El proyecto se puede levantar con Docker en una máquina limpia y los tests corren automáticamente en cada push/PR.

---

### 🧑‍✈️ Yayo – Backend Lead

- Revisar PRs y mantener:
  - Consistencia en nombres de endpoints, DTOs y paquetes.
  - Seguridad (roles, permisos, filtros JWT).
  - Reglas de negocio complejas (stock, estados de orden, validación de reseñas).
- Asegurar que la arquitectura del backend se mantenga limpia y alineada al diseño inicial.

> **DoD Yayo:**  
> Todas las nuevas funcionalidades pasan por revisión técnica, mantienen el estándar del proyecto y no rompen contratos de la API.

---

## Resumen visual (board compacto)

```md
- Max: Login/Registro, catálogo, UI de vendedor.
- Edgar: Órdenes, reseñas, reglas de negocio de dominio.
- Axel: Postman, matriz de pruebas, tests de integración, QA report.
- Emilio: Docker, perfiles de entorno, CI con GitHub Actions, documentación de despliegue.
- Yayo: Revisión técnica, seguridad, reglas avanzadas de negocio.
