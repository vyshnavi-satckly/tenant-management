# Tenant Management

Spring Boot service for tenant management. Team 4 provides the tenant database configuration, connection test, health check, validation, and audit logging features.

## Local setup

The application uses one shared PostgreSQL database: `cloud_platform`. Copy `.env.example` to `.env` and enter the shared local credentials. Do not commit `.env`.

```properties
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/cloud_platform
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=your-password
```

Start the service:

```bash
./mvnw spring-boot:run
```

## Team 4: Tenant database APIs

`{tenantId}` is the UUID from `tenants.id`, not the business value such as `TENANT-002` in `tenants.tenant_id`.

| Method | URL | Purpose |
| --- | --- | --- |
| GET | `/api/tenants/{tenantId}/database` | Read a tenant's database configuration |
| PUT | `/api/tenants/{tenantId}/database` | Create or update a configuration |
| POST | `/api/tenants/{tenantId}/database/test-connection` | Test the shared database connection without saving |
| GET | `/api/tenants/{tenantId}/database/health` | Check the live database connection and stored metrics |

Use this body for `PUT`:

```json
{
  "databaseType": "POSTGRESQL",
  "serverName": "localhost:5432",
  "allocatedStorageGb": 200,
  "autoBackupEnabled": true,
  "maintenanceWindow": "03:00-04:00"
}
```

Use this body for `POST /test-connection`:

```json
{
  "databaseType": "POSTGRESQL",
  "serverName": "localhost:5432"
}
```

The backend assigns `cloud_platform` as the database name. Clients cannot provide a database name or database credentials. Each tenant has one configuration row in `tenant_databases`, but every Team 4 connection check uses the same shared Spring datasource.

## Validation and audit behavior

- Only `POSTGRESQL` is accepted.
- `serverName` must match the host and port in `SPRING_DATASOURCE_URL`.
- `maintenanceWindow` is required.
- Allocated storage must be positive and cannot be below current usage.
- The database connection must be verified before a configuration is saved.
- Tenant database actions are written to `audit_logs`.

For anonymous requests, audit logging dynamically finds the first active, non-deleted user for that tenant from `users`. Each tenant therefore needs at least one active user. When JWT authentication is added, the authenticated user UUID takes priority automatically.

## Test

Run the Team 4 test suite:

```bash
./mvnw -o -Dtest=TenantManagementApplicationTests,TenantDatabaseServiceTests,DatabaseConnectionVerifierTests,TenantDatabaseAuditListenerTests,TenantDatabaseControllerTests test
```

On 2026-09-10, this command completed successfully with 27 tests passing.
