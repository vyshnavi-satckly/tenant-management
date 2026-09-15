package com.tenant_management.repository;

import com.tenant_management.entity.Tenant;
import com.tenant_management.entity.TenantConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class TenantConfigurationRepositoryTest {

    @Autowired
    private TenantConfigurationRepository repository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void shouldSaveAndRetrieveTenantConfiguration() {
        Tenant tenant = new Tenant();
        tenant.setTenantName("Acme Corp");
        tenant = entityManager.persistAndFlush(tenant);

        TenantConfiguration config = TenantConfiguration.builder()
                .tenantId(tenant.getId())
                .country("India")
                .timeZone("Asia/Kolkata")
                .language("en")
                .currency("INR")
                .passwordPolicy("STRONG")
                .sessionTimeoutMinutes(30)
                .mfaEnabled(true)
                .storageLimitGb(BigDecimal.valueOf(50))
                .emailNotificationsEnabled(true)
                .smsNotificationsEnabled(false)
                .build();

        TenantConfiguration saved = repository.save(config);

        assertThat(saved.getId()).isNotNull();
        assertThat(repository.findByTenantId(saved.getTenantId())).isPresent();
    }
}