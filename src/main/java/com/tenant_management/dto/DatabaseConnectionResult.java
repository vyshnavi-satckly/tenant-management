package com.tenant_management.dto;

import java.time.LocalDateTime;

public record DatabaseConnectionResult(boolean connected, String status, String message,
                                       LocalDateTime checkedAt) {}
