# cluster-status-monitor (WRITER)

Microservicio PaaS que **monitorea y persiste** el estado de los clústeres OpenShift
y expone un ABM REST. Es el componente **WRITER** del par WRITER/READER
(su gemelo de sólo lectura es `active-cluster-service`).

- **Java 21** / **Spring Boot 3.5.6** / Maven
- Arquitectura **Hexagonal (Ports & Adapters)**
- MongoDB (estado compartido + cache con TTL)
- Observabilidad: Actuator + Micrometer/Prometheus + tracing OTLP

## Responsabilidades

1. **Scheduler** (`ClusterStatusPollingScheduler`): cada `csm.scheduler.fixed-delay` ms
   sondea la salud de cada clúster registrado, persiste el estado (`app_cluster_status`)
   y refresca el cache (`app_status_cache`). Aísla fallos por clúster (CSM-201) sin
   abortar el ciclo.
2. **ABM REST** (`/api/v1/cluster-status`, `/api/v1/alias-status`): alta/baja/modificación,
   con reglas de negocio (una sola app activa por clúster) y sobre PaaS
   `{ meta, data, errors }`.

## Arquitectura (paquetes)

```
domain/            modelo inmutable + excepciones (códigos CSM-xxx)
application/
  port/in          casos de uso (Poll..., ManageCluster..., ManageAlias...)
  port/out         puertos de salida (Repository, Cache, Probe, Catalog)
  service          orquestación (monitor + ABM)
infrastructure/
  adapter/in/rest        controladores + handler + DTOs
  adapter/in/scheduler   disparador programado
  adapter/out/mongo      documentos, repos, adapters de persistencia y cache
  adapter/out/ocp        probe HTTP (RestClient/httpclient5) + catálogo estático
  config                 Scheduler/Mongo/RestClient/OpenApi + CsmProperties
  mapper                 MapStruct (dominio<->documento, proyección cache)
```

## Clave lógica y cache

- Clave lógica CSM: `codigo:app:cluster` (también usada como `cacheKey`).
- La colección `app_status_cache` tiene índice TTL sobre `expiresAt` (expireAfter 0s).

## Ventana de gracia manual

El upsert programado **no** sobreescribe un registro marcado `updatedBy=MANUAL`
cuyo `lastManualAt` (o `updatedAt`) esté dentro de `csm.manual-grace-seconds`.

## Configuración principal (env)

| Variable | Default | Descripción |
|---|---|---|
| `MONGO_URI_SHARED` | localhost | URI Mongo estado compartido |
| `MONGO_URI_CACHE` | localhost | URI Mongo cache |
| `CSM_SCHEDULER_ENABLED` | true | Habilita el scheduler |
| `CSM_SCHEDULER_FIXED_DELAY` | 30000 | Delay entre ciclos (ms) |
| `CSM_SCHEDULER_INITIAL_DELAY` | 10000 | Delay inicial (ms) |
| `CSM_PROBE_TIMEOUT_MS` | 3000 | Timeout del probe |
| `CSM_PROBE_MAX_RETRIES` | 2 | Reintentos del probe |
| `CSM_PROBE_RETRY_BACKOFF_MS` | 500 | Backoff base entre reintentos |
| `CSM_MANUAL_GRACE_SECONDS` | 300 | Ventana de gracia manual |
| `CACHE_TTL_SECONDS` | 30 | TTL del cache |

## Métricas Prometheus

- `csm_probe_latency_seconds{cluster}` — latencia de cada probe.
- `csm_poll_clusters_total{result=success|failed}` — clústeres por ciclo.
- `csm_poll_cycle_errors_total` — ciclos con error.
- `csm_poll_cycle_duration_seconds` — duración del ciclo.
- `csm_cache_refresh_total{result=updated|invalidated}` — operaciones de cache.
- `csm_abm_requests_total{op,result}` — operaciones ABM.
- `csm_write_conflict_total{source}` — conflictos de escritura optimista.

## Build y ejecución

```bash
mvn clean package
java -jar target/cluster-status-monitor-1.0.0-SNAPSHOT.jar
# Swagger UI: http://localhost:8081/swagger-ui.html
# Actuator:   http://localhost:8081/actuator/health
```

## Códigos de error

| Código | HTTP | Significado |
|---|---|---|
| CSM-001 | 400 | Validación de entrada |
| CSM-004 | 404 | No encontrado |
| CSM-009 | 409 | Conflicto (clave duplicada) |
| CSM-022 | 422 | Regla de negocio |
| CSM-007 | 503 | Persistencia no disponible |
| CSM-201 | warn | Fallo de probe aislado |
| CSM-500 | 500 | Error interno / ciclo |

## Despliegue

Manifiestos en `deploy/openshift/`. **El scheduler debe correr con una única réplica
activa**; para escalar el ABM con el HPA usar ShedLock (lock distribuido en Mongo).
Inicialización de Mongo (colecciones/índices/usuarios) en `mongo/init.js`.
