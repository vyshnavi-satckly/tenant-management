package com.tenant_management.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.UUID;

@Entity
@Table(name = "tenants")
@Data
public class Tenant {
    @Id
    private UUID id;
}