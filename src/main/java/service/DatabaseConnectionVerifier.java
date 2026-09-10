package com.tenant_management.service;

import com.tenant_management.dto.DatabaseConnectionResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class DatabaseConnectionVerifier {
    private final String username;
    private final String password;
    private final Set<String> allowedServers;

    public DatabaseConnectionVerifier(
            @Value("${tenant.database.connection.username:}") String username,
            @Value("${tenant.database.connection.password:}") String password,
            @Value("${tenant.database.connection.allowed-servers:localhost:5432}") String allowedServers) {
        this.username = username;
        this.password = password;
        this.allowedServers = Arrays.stream(allowedServers.split(",")).map(String::trim).collect(Collectors.toSet());
    }

    public void validateTarget(String type, String server) {
        if (type == null || !"POSTGRESQL".equalsIgnoreCase(type.trim()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Supported database type is POSTGRESQL");
        if (server == null || server.length() > 255
                || !server.trim().matches("[a-zA-Z0-9.-]+:[0-9]{1,5}")
                || !allowedServers.contains(server.trim()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Server must be an administrator-approved host:port");
        int port = Integer.parseInt(server.trim().substring(server.trim().lastIndexOf(':') + 1));
        if (port < 1 || port > 65535)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid database port");
    }

    public DatabaseConnectionResult verify(String type, String server, String databaseName) {
        validateTarget(type, server);
        if (databaseName == null || !databaseName.matches("[a-zA-Z_][a-zA-Z0-9_]{0,62}"))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Database name is missing or invalid");
        if (username.isBlank())
            return result(false, "Database verification credentials are not configured");
        Properties properties = new Properties();
        properties.setProperty("user", username);
        properties.setProperty("password", password);
        properties.setProperty("connectTimeout", "5");
        properties.setProperty("socketTimeout", "5");
        try (Connection connection = DriverManager.getConnection(
                "jdbc:postgresql://" + server.trim() + "/" + databaseName, properties)) {
            return result(connection.isValid(5), "Connection check completed");
        } catch (SQLException exception) {
            // SQL exceptions can include infrastructure details; do not expose them to API clients.
            return result(false, "Unable to connect to the tenant database");
        }
    }

    private DatabaseConnectionResult result(boolean connected, String message) {
        return new DatabaseConnectionResult(connected, connected ? "CONNECTED" : "DISCONNECTED", message, LocalDateTime.now());
    }
}
