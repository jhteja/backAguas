# backAguas
Versión académica de Jaime Tejada Proyecto Aguas El Paraiso

API REST desarrollada con **Java** y **Spring Boot** para la gestión de suscriptores, predios y facturación de un acueducto veredal, conectada a **PostgreSQL** y preparada para ejecutarse en contenedor **Docker**.

## 🛠️ Tecnologías

- **Java 25** & **Spring Boot 4**
- **Maven** (con Maven Wrapper `mvnw`)
- **Spring Web** & **Spring Data JPA (Hibernate)**
- **PostgreSQL** (compatible con instancia local, Neon o Render)
- **SpringDoc OpenAPI (Swagger UI)** & **Scalar**
- **Docker**

## 🧱 Arquitectura y Modelo de Datos

El proyecto sigue una arquitectura en capas dentro del paquete `com.example.acueducto`:

- `controller/` — Endpoints REST (`UsuarioController`, `PredioController`, `FacturaController`)
- `service/` — Lógica y validaciones de negocio
- `repository/` — Acceso a datos con Spring Data JPA (`UsuarioRepository`, `PredioRepository`, `FacturaRepository`)
- `model/` — Entidades JPA y enumeraciones (`EstadoFactura`)
- `dto/` — Objetos de transferencia de datos (`RequestDTO`, `ResponseDTO`, `ErrorResponseDTO`)
- `exception/` — Manejo global de excepciones
- `config/` — Configuración de OpenAPI, CORS y siembra inicial de datos

**Relación principal:** `Usuarios` (1:N) ➔ `Predios` (1:N) ➔ `Facturas`.

## 📡 Endpoints Principales

- **Usuarios:** `/api/usuarios`
- **Predios:** `/api/predios`
- **Facturas:** `/api/facturas` (incluye `PATCH /api/facturas/{id}/pagar`)

## ⚙️ Configuración y Ejecución Local

1. **Configura las variables de entorno** (toma como referencia `.env.example` y no subas tus credenciales reales a Git):
   ```env
   PORT=
   DB_URL=
   DB_USERNAME=
   DB_PASSWORD=


Ejecuta la aplicación con el Maven Wrapper:
# Windows
./mvnw.cmd spring-boot:run

# Linux / macOS
./mvnw spring-boot:run
📖 Documentación Interactiva
Con el servidor activo en el puerto 8080, puedes probar los endpoints desde el navegador:
Swagger UI: http://localhost:8080/swagger-ui.html
Scalar: http://localhost:8080/scalar
OpenAPI JSON: http://localhost:8080/v3/api-docs
🐳 Ejecución con Docker
# Construir la imagen
docker build -t acueducto-api .

# Ejecutar el contenedor
docker run -p 8080:8080 \
  -e DB_URL="jdbc:postgresql://<HOST>/<NOMBRE_BD>?sslmode=require" \
  -e DB_USERNAME="<TU_USUARIO>" \
  -e DB_PASSWORD="<TU_CONTRASEÑA>" \
  acueducto-api
