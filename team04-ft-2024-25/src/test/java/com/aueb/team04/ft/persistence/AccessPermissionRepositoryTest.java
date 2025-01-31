package com.aueb.team04.ft.persistence;

import com.aueb.team04.ft.domain.AccessPermission;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
public class AccessPermissionRepositoryTest {
    @Inject
    AccessPermissionRepository accessPermissionRepository;

    @Test
    @Transactional
    public void testSave() {
        AccessPermission accessPermission = new AccessPermission();
        accessPermissionRepository.save(accessPermission);

        assertNotNull(accessPermission.getId());

    }
}
