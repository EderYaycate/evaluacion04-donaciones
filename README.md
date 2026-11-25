# Evaluación N.° 04 — Gestión de donaciones

Arquitectura de microservicios con **Spring Boot 3.5.15**, **Spring Cloud 2025.0.0** y **Java 17**.

**Alumno:** Eder Marcelo Yaycate Bardales · 4 - C24 - Sección A
**Curso:** Desarrollo de Aplicaciones Web · Prof. Ricardo Coello Palomino

## Arquitectura

```
                       ┌──────────────────┐
   Postman ──────────▶ │   api-gateway    │  :8090
                       └────────┬─────────┘
                                │ lb:// (nombre en Eureka)
        ┌───────────────────────┼───────────────────────┐
        ▼                       ▼                       ▼
┌───────────────┐      ┌───────────────┐      ┌─────────────────┐
│donante-service│      │campana-service│      │ donacion-service│
│     :8081     │      │     :8082     │      │      :8083      │
│ bd_donante_dev│      │ bd_campana_dev│      │ bd_donacion_dev │
└───────┬───────┘      └───────┬───────┘      └────────┬────────┘
        │                      │                       │
        │   OpenFeign + Circuit Breaker                 │
        └──────────◀───────────┴──────────◀─────────────┘

                       ┌──────────────────┐
                       │  eureka-server   │  :8761
                       └──────────────────┘
```

`donacion-service` es el único que habla con otros servicios: para devolver una
donación necesita saber **quién** donó (donante-service) y **a qué campaña**
(campana-service). Esas dos llamadas van protegidas con Circuit Breaker.

## Puertos y bases de datos

| Proyecto | Puerto dev | Puerto prod | Base de datos |
|---|---|---|---|
| eureka-server | 8761 | 8761 | — |
| api-gateway | 8090 | 8090 | — |
| donante-service | 8081 | 9081 | `bd_donante_dev` / `bd_donante_prod` |
| campana-service | 8082 | 9082 | `bd_campana_dev` / `bd_campana_prod` |
| donacion-service | 8083 | 9083 | `bd_donacion_dev` / `bd_donacion_prod` |

## Requisitos

- JDK 17
- Maven 3.9+ (o el wrapper de IntelliJ)
- MySQL 8 (XAMPP)

## Instalación

### 1. Bases de datos

Con MySQL encendido:

```bash
mysql -u root < sql/01_bd_donante.sql
mysql -u root < sql/02_bd_campana.sql
mysql -u root < sql/03_bd_donacion.sql
```

Los scripts crean las tres bases, sus tablas y datos de ejemplo. No hay claves
foráneas entre bases: cada microservicio es dueño de la suya y la relación se
resuelve por API.

### 2. Compilar

```bash
cd eureka-server    && mvn clean package -DskipTests && cd ..
cd api-gateway      && mvn clean package -DskipTests && cd ..
cd donante-service  && mvn clean package -DskipTests && cd ..
cd campana-service  && mvn clean package -DskipTests && cd ..
cd donacion-service && mvn clean package -DskipTests && cd ..
```

### 3. Ejecutar (en este orden)

```bash
java -jar eureka-server/target/eureka-server-0.0.1-SNAPSHOT.jar
java -jar donante-service/target/donante-service-0.0.1-SNAPSHOT.jar
java -jar campana-service/target/campana-service-0.0.1-SNAPSHOT.jar
java -jar donacion-service/target/donacion-service-0.0.1-SNAPSHOT.jar
java -jar api-gateway/target/api-gateway-0.0.1-SNAPSHOT.jar
```

Eureka va primero porque los demás se registran al arrancar, y el Gateway al
final porque resuelve sus rutas con los nombres que encuentra en Eureka.

Panel de Eureka: <http://localhost:8761>

### Perfil de ejecución

El perfil activo por defecto es `dev`. Para producción:

```bash
java -jar donante-service/target/donante-service-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```

En `prod` el `ddl-auto` pasa a `validate` (no toca el esquema), se apaga el
`show-sql` y las credenciales salen de las variables `DB_HOST`, `DB_USER` y
`DB_PASSWORD`.

## API

Todo entra por el Gateway, en `http://localhost:8090`.

> El puerto habitual sería 8080, pero en el equipo de desarrollo lo ocupa
> Oracle XE, así que el Gateway escucha en **8090**.

| Método y ruta | Qué hace |
|---|---|
| `GET /api/donantes` | Lista los donantes |
| `GET /api/donantes/{id}` | Un donante · 404 si no existe |
| `POST /api/donantes` | Crea · 400 si falla una validación · 409 si el DNI se repite |
| `PUT /api/donantes/{id}` | Actualiza |
| `DELETE /api/donantes/{id}` | Elimina · 204 |
| `GET /api/campanas` | Lista las campañas |
| `GET /api/campanas/{id}` | Una campaña |
| `POST /api/campanas` | Crea · 400 si la fecha de fin es anterior a la de inicio |
| `PUT /api/campanas/{id}` | Actualiza |
| `DELETE /api/campanas/{id}` | Elimina |
| `GET /api/donaciones` | Lista las donaciones **con su donante y su campaña** |
| `GET /api/donaciones?campanaId={id}` | Filtra por campaña |
| `GET /api/donaciones/{id}` | Una donación completa |
| `POST /api/donaciones` | Registra una donación |
| `PUT /api/donaciones/{id}` | Actualiza |
| `DELETE /api/donaciones/{id}` | Elimina |

## Comunicación entre microservicios

`donacion-service` declara dos clientes Feign que apuntan a **nombres de
Eureka**, no a direcciones:

```java
@FeignClient(name = "donante-service")
public interface DonanteClient {
    @GetMapping("/api/donantes/{id}")
    DonanteDTO obtenerDonante(@PathVariable("id") Long id);
}
```

## Tolerancia a fallos

Cada llamada externa pasa por un Circuit Breaker con su método alternativo:

```java
@CircuitBreaker(name = "donanteService", fallbackMethod = "donanteFallback")
public DonanteDTO obtenerDonante(Long id) { ... }
```

Con `donante-service` apagado, `GET /api/donaciones` **sigue respondiendo 200**
y la donación conserva su monto, su fecha y su campaña; solo el donante baja a
`"Donante no disponible"`.

Estado de los circuitos: <http://localhost:8083/actuator/circuitbreakers>

## Pruebas

`postman/Evaluacion04-Donaciones.postman_collection.json` — 22 peticiones
organizadas en cuatro carpetas: donantes, campañas, donaciones y tolerancia a
fallos. La variable `gateway` ya apunta a `http://localhost:8090`.

## Despliegue

El proyecto incluye un `Dockerfile` por servicio y un `docker-compose.yml` que
levanta el sistema completo:

```bash
docker compose up --build
```

Las instrucciones para desplegar en la nube están en [DESPLIEGUE.md](DESPLIEGUE.md).

## Estructura

```
evaluacion04-donaciones/
├── eureka-server/
├── api-gateway/
├── donante-service/
├── campana-service/
├── donacion-service/
├── sql/
├── postman/
├── docker-compose.yml
├── DESPLIEGUE.md
└── README.md
```
