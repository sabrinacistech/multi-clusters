# active-cluster-service (READER)

Microservicio PaaS de solo lectura para consultar el cluster activo. Arquitectura Hexagonal (Ports & Adapters), Java 21, Spring Boot 3.5.6, MongoDB, virtual threads.

## Build

```bash
mvn clean package
```

El plugin `openapi-generator-maven-plugin` genera las interfaces y DTOs a partir de `src/main/resources/openapi/acs-api.yaml` en la fase `generate-sources`. El controlador se implementa como `@RestController` plano (no implementa la interfaz generada) para garantizar la compilacion.

## Run

```bash
export MONGO_URI_SHARED="mongodb://localhost:27017/acs"
export MONGO_URI_CACHE="mongodb://localhost:27017/acs_cache"
java -jar target/active-cluster-service-1.0.0-SNAPSHOT.jar --spring.profiles.active=dev
```

Perfiles disponibles: `dev`, `int`, `qas`, `prd`.

## Docker

```bash
docker build -t active-cluster-service:1.0.0 .
docker run -p 8080:8080 -e MONGO_URI_SHARED=... active-cluster-service:1.0.0
```

## Endpoints

Base path: `/v1/pass/arqs/cluster-availability-service`

| Metodo | Ruta                  | Operacion            |
|--------|-----------------------|----------------------|
| GET    | `/get-cluster-status` | getClusterStatus     |
| POST   | `/inicial`            | registerInitial      |
| POST   | `/create`             | createClusterState   |
| PUT    | `/update`             | updateClusterState   |

`GET /get-cluster-status` acepta headers opcionales `APP-NAME`, `PROJECT-NAME`, `CLIENT-CLUSTER-ACTIVE`, `ENVIRONMENT`. Devuelve el envelope PaaS:

```json
{
  "meta": { "method": "GET", "operation": "getClusterStatus" },
  "data": { "active": true, "pollingIntervalSeconds": 30 },
  "errors": []
}
```

## Estrategia de lectura

1. Lee primero `app_status_cache` por `cacheKey = application:project:environment` (respetando TTL).
2. Si no hay hit, consulta `app_cluster_status` con proyeccion y `secondaryPreferred`.
3. Si no existe estado -> `ACS-004` (404).

## Codigos de error

| Codigo   | HTTP | Descripcion                    |
|----------|------|--------------------------------|
| ACS-001  | 400  | Parametros invalidos           |
| ACS-004  | 404  | Estado no encontrado           |
| ACS-007  | 503  | Servicio de datos no disponible|
| ACS-500  | 500  | Error interno                  |

## Observabilidad

- Metricas Micrometer: `acs_query_requests_total{result=found|not_found|error}`, `acs_query_latency_seconds`.
- Actuator: `/actuator/health` (liveness/readiness), `/actuator/prometheus`, `/actuator/info`.
- Tracing OpenTelemetry (OTLP).

## OpenShift

Manifiestos en `deploy/openshift/`.
