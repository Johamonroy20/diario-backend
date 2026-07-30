# diario-backend
Backend Spring Boot + JWT para app de journaling digital

# JouOff — Backend

API REST para **JouOff**, una aplicación de journaling emocional con estética vintage. Este repositorio contiene el backend construido con Spring Boot, encargado de la autenticación de usuarios y la gestión de entradas de diario.

## Stack técnico

- **Java 25** + **Spring Boot 4.1.0**
- **Spring Data JPA** — persistencia con PostgreSQL
- **Spring Security** + **JWT** (JJWT) — autenticación stateless
- **Lombok** — reducción de código repetitivo (getters, setters, constructores)
- **Bean Validation** (`jakarta.validation`) — validación de DTOs de entrada
- **JUnit 5 + Mockito + AssertJ** — testing unitario
- **Maven** — gestión de dependencias y build

## Estructura del proyecto

```
src/main/java/com/jouOff/diario_backend/
├── config/         # Configuración de Spring Security, CORS
├── controller/      # Endpoints REST
├── dto/             # Objetos de transferencia (request/response)
├── enums/           # Enumeraciones (ej. EstadoAnimo)
├── model/           # Entidades JPA
├── repository/      # Interfaces Spring Data JPA
├── security/        # JwtUtil, JwtAuthenticationFilter
└── service/         # Lógica de negocio

src/test/java/...     # Tests unitarios (misma estructura de paquetes)
```

## Configuración del entorno

### Requisitos previos

- JDK 17+ (recomendado 21 o superior)
- Maven (se incluye el wrapper `./mvnw`, no requiere instalación separada)
- PostgreSQL 14+ corriendo localmente
- Una base de datos creada, por ejemplo `diario_db`

### Variables de entorno / configuración local

El proyecto usa dos perfiles combinados: `dev` (plantilla versionada) y `local` (credenciales reales, **nunca versionado**).

1. Crea `src/main/resources/application-local.properties` (este archivo está en `.gitignore`) con el siguiente contenido:

```properties
spring.datasource.password=tu_contraseña_real_de_postgres
jwt.secret=una_clave_secreta_larga_y_aleatoria_minimo_32_caracteres
```

2. Genera una clave JWT segura con:
```bash
openssl rand -base64 32
```

3. Confirma que `application.properties` tenga:
```properties
spring.profiles.active=dev,local
```

### Levantar el proyecto

```bash
./mvnw spring-boot:run
```

El servidor arranca en `http://localhost:8080`.

### Correr los tests

```bash
./mvnw test
```

## Endpoints disponibles

| Método | Ruta | Descripción | Autenticación |
|---|---|---|---|
| GET | `/api/ping` | Endpoint de salud | No |
| POST | `/api/auth/registro` | Registro de nuevo usuario | No |
| POST | `/api/auth/login` | Inicio de sesión, devuelve JWT | No |
| POST | `/api/entradas` | Crear entrada de diario | Sí (`Bearer <token>`) |

## Seguridad y autenticación (JWT)

- Contraseñas hasheadas con **BCrypt**, nunca almacenadas en texto plano
- Autenticación **stateless** vía JWT (sin sesiones de servidor)
- El token expira a las **24 horas**
- Cada endpoint protegido valida el token mediante `JwtAuthenticationFilter`, y el `usuarioId` se extrae del propio token — nunca se confía en un ID enviado por el cliente

**Dónde vive cada pieza del flujo de JWT:**

| Archivo | Responsabilidad |
|---|---|
| `security/JwtUtil.java` | Genera y valida el token; extrae `usuarioId` y `email` de sus claims |
| `service/AuthService.java` | Genera el token al hacer login exitoso (`generarToken`) |
| `security/JwtAuthenticationFilter.java` | Intercepta cada petición, valida el token, y registra el usuario autenticado en `SecurityContextHolder` |
| `config/SecurityConfig.java` | Registra el filtro en la cadena de seguridad (`addFilterBefore`) y define qué rutas son públicas |
| Cualquier `*Service` que necesite el usuario actual | Recupera el `usuarioId` desde `SecurityContextHolder.getContext().getAuthentication().getPrincipal()` |

## Convenciones del proyecto

- **Lombok** se usa en toda entidad/servicio/controlador (`@Getter`, `@Setter`, `@NoArgsConstructor`, `@RequiredArgsConstructor`), evitando `@Data` en entidades `@Entity` para prevenir problemas de referencias circulares en relaciones JPA
- **Enums** para conjuntos fijos de valores (ej. `EstadoAnimo`: FELIZ, CALMADO, NEUTRAL, ANSIOSO, TRISTE, ENOJO), guardados como texto en la base de datos (`@Enumerated(EnumType.STRING)`)
- **DTOs de respuesta** (ej. `EntradaResponse`) para nunca exponer datos sensibles del usuario en endpoints relacionados a otras entidades
- Nombres de columnas en base de datos en `snake_case` (ej. `nombre_completo`, `fecha_registro`, `estado_animo`)
- Ramas de feature: `feature/nombre-descriptivo` (sin ID de ticket)
- Commits en inglés, formato de conventional commits (`feat:`, `fix:`, `test:`, `chore:`, `docs:`)

## Estado actual del proyecto

- Configuración de entorno y base de datos (PostgreSQL) — completo
- Registro de usuario — completo
- Login con JWT — completo
- Logout (lógica del lado del cliente) — completo
- Crear entrada de diario (POST /api/entradas) — completo
- CRUD de entradas: listado, detalle, edición y eliminación — pendiente
- Autorización a nivel de recurso (validar que una entrada pertenezca al usuario autenticado en operaciones de lectura/edición/borrado) — pendiente