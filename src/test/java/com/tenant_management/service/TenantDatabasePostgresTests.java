package com.tenant_management.service;

import java.sql.DriverManager;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import static org.junit.jupiter.api.Assertions.*;

/** Opt-in checks against an existing local database; audit writes are rolled back. */
@EnabledIfEnvironmentVariable(named = "TEAM4_TEST_DATABASE", matches = ".+")
class TenantDatabasePostgresTests {
    @Test void verifiesLiveConnection() {
        String name = System.getenv("TEAM4_TEST_DATABASE");
        String server = System.getenv("TEAM4_TEST_SERVER");
        String user = System.getenv("TEAM4_TEST_USER");
        String password = System.getenv("TEAM4_TEST_PASSWORD");
        DatabaseConnectionVerifier verifier = new DatabaseConnectionVerifier(user, password, server);
        assertTrue(verifier.verify("POSTGRESQL", server, name).connected());
        assertFalse(verifier.verify("POSTGRESQL", server, "missing_" + UUID.randomUUID().toString().replace("-", "")).connected());
    }

    @Test void persistsAuditUsingExistingSchema() throws Exception {
        String name = System.getenv("TEAM4_TEST_DATABASE");
        String server = System.getenv("TEAM4_TEST_SERVER");
        String user = System.getenv("TEAM4_TEST_USER");
        String password = System.getenv("TEAM4_TEST_PASSWORD");
        try (var connection = DriverManager.getConnection("jdbc:postgresql://" + server + "/" + name, user, password)) {
            connection.setAutoCommit(false);
            try {
                JdbcTemplate jdbc = new JdbcTemplate(new SingleConnectionDataSource(connection, true));
                var actors = jdbc.queryForList("SELECT id FROM users LIMIT 1", UUID.class);
                org.junit.jupiter.api.Assumptions.assumeFalse(actors.isEmpty(),
                        "Audit integration requires an existing users row for the foreign key");
                UUID actor = actors.getFirst();
                UUID entity = UUID.randomUUID();
                new TenantDatabaseAuditListener(jdbc, actor.toString()).record(
                        new TenantDatabaseAuditEvent(entity, "DATABASE_CONNECTION_TESTED", true, LocalDateTime.now()));
                assertEquals(1, jdbc.queryForObject(
                        "SELECT count(*) FROM audit_logs WHERE entity_id = ? AND user_id = ? AND status = 'SUCCESS'",
                        Integer.class, entity.toString(), actor));
            } finally {
                connection.rollback();
            }
        }
    }
}
