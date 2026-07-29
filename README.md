# PROYECTO INTEGRADOR - PPW1

## INTEGRANTES
- Sebastián Gómez
- Isaac Mora
- Jorge Pizarro

## Descripción
Proyecto integrador desarrollado con Spring Boot. La aplicación proporciona una API REST estructurada de forma modular para la gestión de usuarios, categorías, eventos, registros y reportes. Incorpora seguridad mediante JWT y protección de endpoints (Rate Limiting) apoyada en Redis.

---

##  Instalación y Requisitos Previos


- **Java 17** (o superior).
- **Gradle** (se puede utilizar el wrapper incluido `./gradlew`).
- **Docker** y **Docker Compose** (para levantar la base de datos y Redis).
- **Python 3** (opcional, para ejecutar los scripts auxiliares de pruebas).

**Clonar el repositorio:**
```bash
git clone <url-del-repositorio>
cd icc-ProyectoIntegrador
```

---

##  Variables de Entorno

El proyecto utiliza variables de entorno para su configuración. Crea un archivo `.env` en la raíz del proyecto (al mismo nivel que `docker-compose.yml`) con la siguiente estructura básica:


# Configuración de la Base de Datos
```
DB_HOST=localhost
DB_PORT=5432
DB_NAME=nombre_base_datos
DB_USER=usuario_bd
DB_PASSWORD=password_bd
```
# Seguridad
```
JWT_SECRET=tu_clave_secreta_super_segura_para_jwt_aqui
JWT_EXPIRATION=86400000
```
# Redis (Rate Limiting)
```
REDIS_HOST=localhost
REDIS_PORT=6379
```

---

## Ejecución

**1. Levantar los servicios de infraestructura (Base de Datos y Redis):**
El proyecto incluye scripts de inicialización (`00_create_database.sql`, `V1__initial_schema_and_data.sql`) que se ejecutarán automáticamente al levantar el contenedor de la base de datos.
```bash
docker compose up -d
```

**2. Ejecutar la aplicación Spring Boot:**
Inicia el backend utilizando Gradle desde la terminal:
```bash
./gradlew bootRun
```
La API estará disponible por defecto en: `http://localhost:8080/api`

---

## Pruebas

Además de las pruebas unitarias en `src/test/java`, el proyecto cuenta con un directorio `scripts/` en la raíz que incluye herramientas de validación de rendimiento y seguridad.

**Prueba de Límites de Peticiones (Rate Limiting):**
Para verificar que los filtros de Redis estén bloqueando correctamente el exceso de peticiones (`429 Too Many Requests`), ejecuta el script de Python:

```bash
# Instalar dependencias de Python si es necesario
pip install requests

# Ejecutar la prueba
python scripts/test_rate_limits.py
```

*Nota útil:* Si necesitas reiniciar los contadores de intentos durante las pruebas, limpia la caché de Redis con:
```bash
docker compose exec redis redis-cli FLUSHALL
```

---

## Despliegue

Para preparar la aplicación para un entorno de producción:

1. **Construir el artefacto (JAR):**
   Ejecuta el siguiente comando para compilar el proyecto empaquetado, omitiendo las pruebas locales si es necesario:
   ```bash
   ./gradlew clean build -x test
   ```
   El archivo `.jar` generado se ubicará en la carpeta `build/libs/`.

2. **Contenedores de Producción:**
   Para entornos productivos, el proyecto utiliza un enfoque basado en Docker. La orquestación permite levantar la imagen compilada junto con todos sus servicios dependientes (base de datos, Redis), utilizando un archivo `.env` adaptado para conectar con los recursos e infraestructura reales del servidor.