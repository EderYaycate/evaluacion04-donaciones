# Despliegue

El sistema se empaqueta con Docker. Cada proyecto tiene su `Dockerfile` en dos
etapas: la primera compila con Maven y la segunda deja solo el JRE y el jar, así
la imagen final pesa poco.

Toda la configuración que cambia entre ambientes sale de variables de entorno,
de modo que el mismo jar sirve en local y en la nube.

## Variables de entorno

| Variable | Para qué sirve | Ejemplo |
|---|---|---|
| `PORT` | Puerto donde escucha el servicio. Las plataformas lo inyectan solas | `8081` |
| `SPRING_PROFILES_ACTIVE` | Perfil activo | `prod` |
| `DB_URL` | Cadena JDBC completa de la base del servicio | `jdbc:mysql://host:3306/bd_donante_prod` |
| `DB_USER` / `DB_PASSWORD` | Credenciales de la base | `root` |
| `EUREKA_URL` | Dónde está el registro | `http://eureka-server:8761/eureka` |
| `SERVICE_HOSTNAME` | Nombre con el que el servicio se anuncia en Eureka | `donante-service` |
| `PREFER_IP` | `true` si la plataforma enruta por IP en vez de por nombre | `false` |

`SERVICE_HOSTNAME` es la pieza delicada: Eureka guarda la dirección que cada
servicio declara, y el Gateway usa esa dirección para llamarlo. Si se deja el
valor por defecto (`localhost`), en la nube el Gateway intentaría llamarse a sí
mismo.

## Opción A — Todo junto con Docker Compose

```bash
docker compose up --build
```

Levanta MySQL (con los tres scripts de `sql/` ejecutados al crearse), Eureka,
los tres microservicios y el Gateway. MySQL se publica en el **3307** para no
chocar con el de XAMPP.

- Gateway: <http://localhost:8090>
- Eureka: <http://localhost:8761>

Para apagarlo: `docker compose down` (añadir `-v` para borrar también los datos).

## Opción B — Railway

Railway tiene MySQL gestionado, red privada entre servicios y despliegue directo
desde un `Dockerfile`, que es lo que este proyecto necesita.

### 1. Subir el código a GitHub

Railway despliega desde un repositorio.

### 2. Crear el proyecto y la base

1. En Railway: **New Project → Deploy from GitHub repo** y elegir el repositorio.
2. **New → Database → MySQL**. Railway crea la instancia y expone sus datos de
   conexión como variables.
3. Dentro de esa base, crear los tres esquemas: `bd_donante_prod`,
   `bd_campana_prod` y `bd_donacion_prod`.

> Cada microservicio sigue teniendo **su propia base de datos lógica**: son tres
> esquemas separados y ningún servicio consulta las tablas de otro. Se comparte
> la instancia, no los datos.

### 3. Crear los cinco servicios

Por cada proyecto: **New → GitHub Repo**, y en *Settings → Root Directory*
indicar la carpeta (`eureka-server`, `api-gateway`, etc.). Railway detecta el
`Dockerfile` solo.

**Orden de despliegue:** primero `eureka-server`, luego los tres microservicios y
al final `api-gateway`.

### 4. Variables por servicio

**eureka-server**

```
EUREKA_HOSTNAME = eureka-server.railway.internal
```

**donante-service** (igual para campaña y donación, cambiando el nombre y la base)

```
SPRING_PROFILES_ACTIVE = prod
EUREKA_URL             = http://eureka-server.railway.internal:8761/eureka
SERVICE_HOSTNAME       = donante-service.railway.internal
DB_URL                 = jdbc:mysql://${{MySQL.MYSQLHOST}}:${{MySQL.MYSQLPORT}}/bd_donante_prod?serverTimezone=America/Lima
DB_USER                = ${{MySQL.MYSQLUSER}}
DB_PASSWORD            = ${{MySQL.MYSQLPASSWORD}}
```

**api-gateway**

```
EUREKA_URL       = http://eureka-server.railway.internal:8761/eureka
SERVICE_HOSTNAME = api-gateway.railway.internal
```

Solo el `api-gateway` necesita dominio público (**Settings → Generate Domain**).
Los demás se quedan en la red privada, que es justamente lo que un Gateway
busca: una sola puerta de entrada.

### 5. Comprobar

```
https://<dominio-del-gateway>/api/donantes
https://<dominio-del-gateway>/api/campanas
https://<dominio-del-gateway>/api/donaciones
```

## Cuidado con el plan gratuito

Son cinco servicios Java más una base. Cada servicio Spring Boot pide entre 300
y 500 MB de memoria, así que el conjunto consume bastante para una cuenta
gratuita. Conviene revisar el crédito disponible antes de desplegarlo todo.

En Render el problema es otro: los servicios gratuitos se duermen a los 15
minutos de inactividad, y cuando Eureka despierta ha perdido los registros, de
modo que el Gateway deja de resolver los nombres hasta que cada servicio se
vuelve a registrar.
