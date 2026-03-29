# quarkus-api-demo

Projet de démonstration illustrant l'utilisation de [quarkus-utils-commons](https://github.com/samira-dev/quarkus-utils-communs) dans une API Quarkus réelle.

## Ce que démontre ce projet

| Fonctionnalité | Classe |
|---|---|
| `@MeasuredEndpoint` — métriques Prometheus automatiques | `UserResource` |
| `PageRequest` / `PageResponse` — pagination générique | `UserPageRequest`, `UserResource` |
| Architecture hexagonale | `domain/`, `infrastructure/`, `api/` |
| `@ApiResponsesDefault` — réponses Swagger mutualisées | `UserResource` |
| `ApiKeyAuthFilter` — sécurité par clé API | `api/filter/` |
| Logging MDC structuré | `shared/logging/` |

## Démarrage rapide

```bash
./mvnw quarkus:dev
```

L'application démarre sur `http://localhost:8080`.

## Endpoints disponibles

| Méthode | URL | Description |
|---|---|---|
| GET | `/api/v1/users` | Liste paginée |
| GET | `/api/v1/users/uuid/{uuid}` | Recherche par UUID |
| POST | `/api/v1/users` | Création |
| PATCH | `/api/v1/users/uuid/{uuid}` | Mise à jour partielle |
| DELETE | `/api/v1/users/uuid/{uuid}` | Suppression |
| GET | `/swagger-ui` | Documentation interactive |
| GET | `/q/metrics` | Métriques Prometheus |
| GET | `/q/health` | Health check |

## Authentification

Tous les endpoints (sauf `/q/*`, `/swagger-ui`, `/api/v1/ping`) requièrent le header :

```
X-API-KEY: demo-secret-key
```

## Exemples curl

```bash
# Lister les utilisateurs
curl -H "X-API-KEY: demo-secret-key" http://localhost:8080/api/v1/users

# Créer un utilisateur
curl -X POST -H "X-API-KEY: demo-secret-key" -H "Content-Type: application/json" \
  -d '{
    "nom": "Durand",
    "prenom": "Alice",
    "siret": "35600000048004",
    "numeroFiness": "123456789",
    "statutJuridique": "SARL",
    "creePar": "MON_APP"
  }' http://localhost:8080/api/v1/users

# Consulter les métriques Prometheus
curl http://localhost:8080/q/metrics | grep api_endpoint
```

## Métriques générées par @MeasuredEndpoint

```
api_endpoint_latency_seconds{endpoint="listAll", operation="user.list"}
api_endpoint_calls_total{endpoint="create",   operation="user.create"}
api_endpoint_errors_total{endpoint="findByUuid", operation="user.findByUuid", exception="ResourceNotFoundException"}
```

## Stack technique

- Java 17
- Quarkus 3.8
- H2 (base de données en mémoire pour la démo)
- MapStruct, Lombok
- quarkus-health-commons 1.0.0
