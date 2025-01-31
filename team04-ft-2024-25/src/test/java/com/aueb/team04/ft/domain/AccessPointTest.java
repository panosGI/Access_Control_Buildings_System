package com.aueb.team04.ft.domain;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import jakarta.persistence.EntityManager;
import static org.junit.jupiter.api.Assertions.*;
import java.util.HashSet;
import java.util.Set;

@QuarkusTest
public class AccessPointTest {
    @Inject
    EntityManager em;

    private Building building;

    @Test
    void testAccessPermissionDefaultConstructor() {
        AccessPoint accessPoint = new AccessPoint();
        assertNotNull(accessPoint);
    }

    @Transactional
    @Test
    void testPersistAccessPoint() {
        Address address = new Address();
        address.setZipcode("12345");

        Building building = new Building();
        building.setBelongsTo("AUEB");
        building.setAddress(address);
        building.setName("Main Building");

        AccessPoint accessPoint = new AccessPoint();
        accessPoint.setLocation("ServerRoom");
        accessPoint.setAccessLevelRequired("3");
        accessPoint.setType(AccessPointType.INPUT);
        accessPoint.setBuilding(building);

        em.persist(building);
        em.persist(accessPoint);

        AccessPoint retrieved = em.find(AccessPoint.class, accessPoint.getId());
        assertNotNull(retrieved);
        assertEquals("ServerRoom", retrieved.getLocation());
    }

    @Test
    public void testSettersAndGetters() {
        AccessPoint accessPoint = new AccessPoint();
        accessPoint.setLocation("Main Entrance");
        accessPoint.setType(AccessPointType.INPUT);
        accessPoint.setAccessLevelRequired("3");

        assertEquals("Main Entrance", accessPoint.getLocation(), "Location should match the set value");
        assertEquals(AccessPointType.INPUT, accessPoint.getType(), "Type should match the set value");
        assertEquals("3", accessPoint.getAccessLevelRequired(), "Access level required should match the set value");
    }

    @Test
    public void testSetAndGetAccessLogs() {
        AccessLog accessLog1 = new AccessLog();
        AccessLog accessLog2 = new AccessLog();
        accessLog1.setAccessStatus(true);
        accessLog2.setAccessStatus(false);
        Set<AccessLog> accessLogs = new HashSet<>();
        accessLogs.add(accessLog1);
        accessLogs.add(accessLog2);

        AccessPoint accessPoint = new AccessPoint();
        accessPoint.setAccessLogs(accessLogs);
        assertEquals(accessLogs, accessPoint.getAccessLogs(), "AccessPoints should match the set values.");
        assertEquals(2, accessPoint.getAccessLogs().size(), "AccessPoints should contain exactly 2 items.");
    }

    @Test
    public void testSetAndGetBuilding() {
        AccessPoint accessPoint = new AccessPoint();
        accessPoint.setBuilding(building);
        assertEquals(building, accessPoint.getBuilding(), "Building should match the set value");
    }

    @Test
    public void testSetAndGetPrerequisites() {

        AccessPoint prerequisite1 = new AccessPoint();
        AccessPoint prerequisite2 = new AccessPoint();
        Set<AccessPoint> accessPoints = new HashSet<>();
        prerequisite1.setLocation("serverRoom1");
        prerequisite2.setLocation("serverRoom2");
        accessPoints.add(prerequisite1);
        accessPoints.add(prerequisite2);

        AccessPoint accessPoint = new AccessPoint();
        accessPoint.setPrerequisites(accessPoints);
        assertEquals(accessPoints, accessPoint.getPrerequisites(), "AccessPoints should match the set values.");
        assertEquals(2, accessPoint.getPrerequisites().size(), "AccessPoints should contain exactly 2 items.");
    }

    @Test
    public void testAddPrerequisite() {

        AccessPoint accessPoint = new AccessPoint();
        AccessPoint prerequisite = new AccessPoint();
        accessPoint.addPrerequisite(prerequisite);

        assertTrue(accessPoint.getPrerequisites().contains(prerequisite), "Prerequisites should contain the added prerequisite");
        assertEquals(1, accessPoint.getPrerequisites().size(), "Prerequisites should contain exactly 1 item");
    }

    @Test
    public void testRemovePrerequisite() {
        AccessPoint accessPoint = new AccessPoint();
        AccessPoint prerequisite = new AccessPoint();
        accessPoint.addPrerequisite(prerequisite);
        accessPoint.removePrerequisite(prerequisite);

        assertFalse(accessPoint.getPrerequisites().contains(prerequisite), "Prerequisites should not contain the removed prerequisite");
        assertEquals(0, accessPoint.getPrerequisites().size(), "Prerequisites should be empty after removal");
    }

    @Test
    public void testAddAccessLog() {
        AccessLog log = new AccessLog();
        AccessPoint accessPoint = new AccessPoint();
        accessPoint.addAccessLog(log);

        assertTrue(accessPoint.getAccessLogs().contains(log), "AccessLogs should contain the added log");
        assertEquals(1, accessPoint.getAccessLogs().size(), "AccessLogs should contain exactly 1 item");
    }

    @Test
    public void testRemoveAccessLog() {
        AccessLog log = new AccessLog();
        AccessPoint accessPoint = new AccessPoint();
        accessPoint.addAccessLog(log);
        accessPoint.removeAccessLog(log);

        assertFalse(accessPoint.getAccessLogs().contains(log), "AccessLogs should not contain the removed log");
        assertEquals(0, accessPoint.getAccessLogs().size(), "AccessLogs should be empty after removal");
    }

}