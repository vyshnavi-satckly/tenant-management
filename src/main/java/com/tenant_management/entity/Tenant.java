package com.tenant_management.entity;
//
//<<<<<<< HEAD
//import jakarta.persistence.*;
//import lombok.Data;
//import java.util.UUID;
//
//@Entity
//@Table(name = "tenants")
//@Data
//public class Tenant {
//    @Id
//    private UUID id;
//}



import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "tenants")
@Data

public class Tenant
{
	@Id
	  @GeneratedValue(strategy = GenerationType.UUID)
	  private UUID id;

	  @Column(name = "name", nullable = false, length = 100)
	  private String tenantname;

	  @Column(name = "status", nullable = false, length = 50)
	  private String status;

	  @Column(name = "created_at", nullable = false)
	  private LocalDateTime createdAt;

	  @Column(name = "created_by")
	  private UUID createdBy;

	  @Column(name = "updated_at")
	  private LocalDateTime updatedAt;

	  @Column(name = "updated_by")
	  private UUID updatedBy;


//    @Id
//    @GeneratedValue(strategy = GenerationType.UUID)
//    private UUID id;
//
//    @Column(name = "name", nullable = false, length = 100)
//    private String tenantname;
    

  
}

	
	

