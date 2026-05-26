# Cluster Status Service

Microservicio Spring Boot para exponer el estado actual del cluster desde una base de datos MongoDB.

## Stack

- Java 21
- Spring Boot 3.3.5
- Maven

## Ejecutar

```bash
mvn spring-boot:run
```

Por defecto intenta conectarse a `mongodb://localhost:27017/multi-clusters`. Se puede cambiar con:

```bash
MONGODB_URI=mongodb://user:password@host:27017/multi-clusters mvn spring-boot:run
```

## MongoDB

El endpoint lee la coleccion `cluster_status` y toma el documento mas reciente para el `cluster.data-center` configurado.

Documento esperado:

```json
{
  "dataCenter": "default",
  "active": true,
  "pollingIntervalSeconds": 30,
  "updatedAt": "2026-05-26T00:00:00Z"
}
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
