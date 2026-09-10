package com.tenant_management.service;

import com.tenant_management.dto.DatabaseConnectionResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.net.URI;
import javax.sql.DataSource;

@Component
public class DatabaseConnectionVerifier {
    private final DataSource dataSource;
    private final String configuredServer;

    public DatabaseConnectionVerifier(DataSource dataSource,
            @Value("${spring.datasource.url}") String datasourceUrl) {
        this.dataSource = dataSource;
        this.configuredServer = serverFromJdbcUrl(datasourceUrl);
    }

    public void validateTarget(String type, String server) {
        if (type == null || !"POSTGRESQL".equalsIgnoreCase(type.trim()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Supported database type is POSTGRESQL");
        if (server == null || !configuredServer.equalsIgnoreCase(server.trim()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Server must match the configured shared database host:port");
    }

    public DatabaseConnectionResult verify(String type, String server, String databaseName) {
        validateTarget(type, server);
        if (databaseName == null || !databaseName.matches("[a-zA-Z_][a-zA-Z0-9_]{0,62}"))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Database name is missing or invalid");
        try (Connection connection = dataSource.getConnection()) {
            return result(connection.isValid(5), "Connection check completed");
        } catch (SQLException exception) {
            return result(false, "Unable to connect to the shared database");
        }
    }

    private String serverFromJdbcUrl(String datasourceUrl) {
        try {
            URI uri = URI.create(datasourceUrl.substring("jdbc:".length()));
            if (!"postgresql".equalsIgnoreCase(uri.getScheme()) || uri.getHost() == null)
                throw new IllegalArgumentException();
            return uri.getHost() + ":" + (uri.getPort() == -1 ? 5432 : uri.getPort());
        } catch (RuntimeException exception) {
            throw new IllegalArgumentException("spring.datasource.url must be a PostgreSQL JDBC URL", exception);
        }
    }

    private DatabaseConnectionResult result(boolean connected, String message) {
        return new DatabaseConnectionResult(connected, connected ? "CONNECTED" : "DISCONNECTED", message, LocalDateTime.now());
    }
}
