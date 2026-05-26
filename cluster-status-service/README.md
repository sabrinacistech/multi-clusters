# Cluster Status Service

Microservicio Spring Boot para exponer el estado actual del cluster desde un store en memoria.

## Stack

- Java 21
- Spring Boot 3.3.5
- Maven

## Ejecutar

```bash
mvn spring-boot:run
```

## Endpoint

```http
GET /get-cluster-status
```

Headers opcionales:

- `APP-NAME`
- `PROJECT-NAME`
- `CLIENT-CLUSTER-ACTIVE`

Respuesta exitosa:

```json
{
  "success": true,
  "message": "Cluster status retrieved successfully",
  "metadata": {
    "appName": "consumer-api",
    "projectName": "multi-clusters",
    "clientClusterActive": "true",
    "cluster": "default",
    "method": "GET",
    "path": "/get-cluster-status",
    "timestamp": "2026-05-26T12:00:00Z"
  },
  "data": {
    "active": true,
    "pollingIntervalSeconds": 30
  }
}
```
