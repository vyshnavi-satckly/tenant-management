package com.tenant_management.service;

import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;
import static org.junit.jupiter.api.Assertions.*;

class DatabaseConnectionVerifierTests {
    private final DatabaseConnectionVerifier verifier = new DatabaseConnectionVerifier("", "", "localhost:5432");

    @Test void missingCredentialsNeverReportsSuccess() {
        assertFalse(verifier.verify("POSTGRESQL", "localhost:5432", "tenant_test").connected());
    }
    @Test void rejectsUnapprovedHostsAndJdbcParameterInjection() {
        assertThrows(ResponseStatusException.class, () -> verifier.verify("POSTGRESQL", "unknown:5432", "tenant_test"));
        assertThrows(ResponseStatusException.class, () -> verifier.verify("POSTGRESQL", "localhost:5432/db?user=admin", "tenant_test"));
        assertThrows(ResponseStatusException.class, () -> verifier.verify("POSTGRESQL", "localhost:5432", "db?user=admin"));
    }
    @Test void rejectsUnsupportedDatabaseTypes() {
        assertThrows(ResponseStatusException.class, () -> verifier.verify("MYSQL", "localhost:5432", "tenant_test"));
    }
}
