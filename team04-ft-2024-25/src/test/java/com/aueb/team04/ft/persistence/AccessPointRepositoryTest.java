package com.aueb.team04.ft.persistence;

import com.aueb.team04.ft.domain.AccessPoint;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
public class AccessPointRepositoryTest {
    @Inject
    AccessPointRepository accessPointRepository;

    @Test
    public void testGetAccessPointById() {
        assertNotNull(accessPointRepository.getAccessPointById(1111L));
    }

    @Test
    public void getAccessPoints() {
        assertNotNull(accessPointRepository.getAccessPoints());
    }

    @Test
    @Transactional
    public void saveAccessPoint() {
        AccessPoint accessPoint = new AccessPoint();
        accessPointRepository.saveAccessPoint(accessPoint);
        assertNotNull(accessPointRepository.getAccessPointById(accessPoint.getId()));
    }
}
