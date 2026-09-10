# Team 4B — Day 1 and Day 2

Branch: `team-4-database-suhana`, based on local `team-4-database`.

## Day 1 integration

Reuses member 4A's TenantDatabase entity and request/response DTOs. Adds a tenant-scoped repository lookup and an explicit unique tenant FK for the one-to-one mapping. Existing source files declare the application package, so Spring discovers them despite their directory layout.

The backend assigns `cloud_platform` as the shared database name and never accepts a database name from the request. Every tenant receives its own read-only `tenant_databases` configuration record while connection and health checks target the shared database.

## Day 2 endpoints

All paths start with `/api/tenants/{tenantId}/database`. `tenantId` is the tenant row UUID, not the business identifier.

| Method | Suffix | Behavior |
| --- | --- | --- |
| GET | (none) | Read stored configuration; 404 for missing tenant/configuration |
| PUT | (none) | Create or update configuration after verifying the candidate connection |
| POST | `/test-connection` | Test candidate settings without saving; returns connected/status/message/checkedAt |
| GET | `/health` | Live connection check plus last recorded storage and utilization metrics |
| POST | (none) | Retained existing create endpoint; 409 if configuration exists |

PUT/create body:

```json
{
  "databaseType": "POSTGRESQL",
  "serverName": "localhost:5432",
  "allocatedStorageGb": 25,
  "autoBackupEnabled": true,
  "maintenanceWindow": "Sunday 02:00-03:00 UTC"
}
```

The connection test only needs `databaseType` and `serverName`. Every save rechecks the actual destination with JDBC; a previous test or client-supplied status cannot bypass verification. Invalid maintenance/storage/settings return 400. Missing or soft-deleted tenants return 404. Allocation must be positive and at least recorded usage. Available storage is recalculated after save.

## Connection configuration

Team 4 uses Spring Boot's existing `spring.datasource` connection pool for the shared `cloud_platform` database. No separate Team 4 database username, password, or allowed-server settings are required. The request `serverName` must match the host and port in `spring.datasource.url` (for local development, `localhost:5432`). PostgreSQL is the supported type. The verifier uses `Connection.isValid(5)` and never returns raw database errors.

Every tenant receives its own configuration row but all connection and health checks target the shared `cloud_platform` database. Health CPU/memory fields remain null until an external collector records them; storage is recorded metadata, not live disk measurement.

## Shared audit persistence

`TenantDatabaseAuditListener` persists events to the existing PostgreSQL `audit_logs` table, whose schema was inspected locally: `id`, `user_id`, `action`, `module`, `entity`, `entity_id`, `status`, and `audit_timestamp` (DATE). No replacement table is created. Configuration saves, reads, completed connection tests, and health checks are audited. Unsuccessful connection/health checks use FAILURE status. Rejected validation/save attempts do not produce committed audit records.

The synchronous listener shares the service transaction: audit failures roll back configuration changes. `entity_id` identifies the tenant UUID. The actor is the authenticated principal name interpreted as a UUID; for unauthenticated/system operation configure `TENANT_DATABASE_AUDIT_SYSTEM_USER_ID` with a valid existing `users.id`. The current application's security configuration permits anonymous requests, so local operation requires this explicit system identity. No user identity is fabricated or accepted from a request body. Missing/invalid actor configuration returns 503; an unknown UUID is rejected by the existing database foreign key. Authentication integration must supply the real user UUID when available.

## Verification

```bash
./mvnw -o -DskipTests compile
./mvnw -o -Dtest=TenantDatabaseServiceTests,DatabaseConnectionVerifierTests,TenantDatabaseAuditListenerTests,TenantDatabaseControllerTests test
```

Focused tests cover tenant existence/deletion, storage limits, creation and update validation, verification before save, stable/generated names, duplicate create, connection testing, health, destination validation, HTTP routing/error statuses, and audit persistence/actor handling.

An opt-in `TenantDatabasePostgresTests` checks real successful/failed JDBC connections and an audit insert using the existing schema. It rolls back the inserted record and requires an existing user. Set `TEAM4_TEST_DATABASE`, `TEAM4_TEST_SERVER` (host:port), `TEAM4_TEST_USER`, and `TEAM4_TEST_PASSWORD`, then run `./mvnw -o -Dtest=TenantDatabasePostgresTests test`. Full tenant lifecycle integration with the other teams remains a separate integration check.

Verification on 2026-09-10: build succeeded; 25 tests passed and one audit integration test was skipped because the local `users` table is empty. Real PostgreSQL success/failure connection checks passed. Audit persistence has unit coverage; a successful live audit insert remains unverified until an existing user is available. Runtime also requires connection credentials and an audit actor configured as described above.
