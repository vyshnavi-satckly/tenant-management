package com.tenant_management.service;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DatabaseConnectionVerifierTests {
    private DataSource dataSource;
    private DatabaseConnectionVerifier verifier;

    @BeforeEach void setup() {
        dataSource = mock(DataSource.class);
        verifier = new DatabaseConnectionVerifier(dataSource,
                "jdbc:postgresql://localhost:5432/cloud_platform");
    }

    @Test void returnsDisconnectedWhenSharedDatasourceCannotConnect() throws Exception {
        when(dataSource.getConnection()).thenThrow(new SQLException("Unavailable"));
        assertFalse(verifier.verify("POSTGRESQL", "localhost:5432", "cloud_platform").connected());
    }
    @Test void validatesTheConfiguredSharedDatabaseHostAndDatabaseName() {
        assertThrows(ResponseStatusException.class, () -> verifier.verify("POSTGRESQL", "unknown:5432", "cloud_platform"));
        assertThrows(ResponseStatusException.class, () -> verifier.verify("POSTGRESQL", "localhost:5432/db?user=admin", "tenant_test"));
        assertThrows(ResponseStatusException.class, () -> verifier.verify("POSTGRESQL", "localhost:5432", "db?user=admin"));
    }
    @Test void rejectsUnsupportedDatabaseTypes() {
        assertThrows(ResponseStatusException.class, () -> verifier.verify("MYSQL", "localhost:5432", "cloud_platform"));
    }

    @Test void reportsConnectionHealthUsingTheSharedDatasource() throws Exception {
        Connection connection = mock(Connection.class);
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.isValid(5)).thenReturn(true);
        assertTrue(verifier.verify("POSTGRESQL", "localhost:5432", "cloud_platform").connected());
        verify(connection).close();
    }

}
