package com.aueb.team04.ft.persistence;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static io.smallrye.common.constraint.Assert.assertNotNull;

@QuarkusTest
public class AdminRepositoryTest {
    @Inject
    AdminRepository adminRepository;

    @Test
    public void findByUsername() {
        assertNotNull(adminRepository.findByUsername("admin5555"));
    }
}
