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
public class BuildingTest {
    @Inject
    EntityManager em;

    @Test
    public void testDefaultConstructor() {
        Building building = new Building();    
        assertNotNull(building);
    }

    @Test
    void testBuildingConstructor() {
        Address address = new Address();
        address.setZipcode("12345");
        Building building = new Building("Main Building", "AUEB", address);
        assertNotNull(building);
    }

    @Test
    void testBuildingConstructorNull() {
        Building building = new Building(null, null, null);
        assertNotNull(building);
    }

    @Transactional
    @Test
    void testPersistBuilding() {
        Address address = new Address();
        address.setZipcode("12345");
        Building building = new Building();
        building.setBelongsTo("AUEB");
        building.setAddress(address);
        building.setName("Main Building0101010TEST");
        em.persist(building);

        Building retrieved = em.find(Building.class, building.getId());
        assertNotNull(retrieved);
        assertEquals("AUEB", retrieved.getBelongsTo());
    }

    @Test
    public void testSettersAndGetters() {
        Address address = new Address();
        address.setZipcode("12345");
        address.setStreetName("Main St");
        address.setStreetNumber("1");

        Building building = new Building();
        building.setAddress(address);
        building.setBelongsTo("AUEB");
        building.setName("Main Building");

        assertEquals(address, building.getAddress(), "Address should match the set value.");
        assertEquals("AUEB", building.getBelongsTo(), "BelongsTo should match the set value.");
        assertEquals("Main Building", building.getName(), "Name should match the set value.");
    }

    @Test
    public void testSetAndGetAccessPermissions() {
        AccessPermission permission1 = new AccessPermission();
        AccessPermission permission2 = new AccessPermission();
        Set<AccessPermission> permissions = new HashSet<>();
        permission1.setAccessLevel("1");
        permission2.setAccessLevel("3");
        permissions.add(permission1);
        permissions.add(permission2);

        Building building = new Building();
        building.setAccessPermissions(permissions);
        assertEquals(permissions, building.getAccessPermissions(), "AccessPermissions should match the set values.");
        assertEquals(2, building.getAccessPermissions().size(), "AccessPermissions should contain exactly 2 items.");
    }

    @Test
    public void testSetAndGetAccessPoints() {
        AccessPoint point1 = new AccessPoint();
        AccessPoint point2 = new AccessPoint();
        point1.setLocation("serverRoom1");
        point2.setLocation("serverRoom2");
        Set<AccessPoint> accessPoints = new HashSet<>();
        accessPoints.add(point1);
        accessPoints.add(point2);

        Building building = new Building();
        building.setAccessPoints(accessPoints);
        assertEquals(accessPoints, building.getAccessPoints(), "AccessPoints should match the set values.");
        assertEquals(2, building.getAccessPoints().size(), "AccessPoints should contain exactly 2 items.");
    }

    @Test
    public void testEmptyAccessPermissionsAndPoints() {
        Building building = new Building();
        building.setAccessPermissions(new HashSet<>());
        building.setAccessPoints(new HashSet<>());

        assertTrue(building.getAccessPermissions().isEmpty(), "AccessPermissions should be empty when initialized with an empty set.");
        assertTrue(building.getAccessPoints().isEmpty(), "AccessPoints should be empty when initialized with an empty set.");
    }

    @Transactional
    @Test
    void testAccessPermissionAssociation() {
        AccessPermission accessPermission = new AccessPermission();
        Address address = new Address();
        Building building = new Building();
        building.setAddress(address);
        building.setName("Building A2");
        building.setBelongsTo("Owner A");
        accessPermission.setAccessLevel("1");
        accessPermission.setBuilding(building);

        em.persist(building);

        Building retrieved = em.find(Building.class, building.getId());
        assertNotNull(retrieved.getAccessPermissions());
    }

    @Transactional
    @Test
    void testAccessPointAssociation() {
        AccessPoint accessPoint = new AccessPoint();
        Address address = new Address();
        Building building = new Building();
        building.setAddress(address);
        building.setName("Building A00010101");
        building.setBelongsTo("Owner A");
        accessPoint.setBuilding(building);

        em.persist(building);

        Building retrieved = em.find(Building.class, building.getId());
        assertNotNull(retrieved.getAccessPoints());
    }

    @Test
    public void testSetAccessPermissionsNull() {
        Building building = new Building();
        building.setAccessPermissions(null);
        assertNull(building.getAccessPermissions(), "AccessPermissions should be null if explicitly set to null.");
    }

    @Test
    public void testSetAccessPointsNull() {
        Building building = new Building();
        building.setAccessPoints(null);
        assertNull(building.getAccessPoints(), "AccessPoints should be null if explicitly set to null.");
    }

    @Test
    public void testAddSingleAccessPermission() {
        Building building = new Building();
        AccessPermission permission = new AccessPermission();
        building.addAccessPermission(permission);

        assertTrue(building.getAccessPermissions().contains(permission), "AccessPermissions should contain the added permission.");
        assertEquals(1, building.getAccessPermissions().size(), "AccessPermissions should contain exactly 1 item.");
    }

    @Test
    public void testRemoveSingleAccessPermission() {
        AccessPermission permission = new AccessPermission();
        Building building = new Building();
        building.addAccessPermission(permission);
        building.removeAccessPermission(permission);

        assertFalse(building.getAccessPermissions().contains(permission), "AccessPermissions should not contain the removed permission.");
        assertEquals(0, building.getAccessPermissions().size(), "AccessPermissions should be empty after removal.");
    }

    @Test
    public void testAddSingleAccessPoint() {
        AccessPoint point = new AccessPoint();
        Building building = new Building();
        building.addAccessPoint(point);
        assertTrue(building.getAccessPoints().contains(point), "AccessPoints should contain the added access point.");
        assertEquals(1, building.getAccessPoints().size(), "AccessPoints should contain exactly 1 item.");
    }

    @Test
    public void testRemoveSingleAccessPoint() {
        AccessPoint point = new AccessPoint();
        Building building = new Building();
        building.addAccessPoint(point);
        building.removeAccessPoint(point);

        assertFalse(building.getAccessPoints().contains(point), "AccessPoints should not contain the removed access point.");
        assertEquals(0, building.getAccessPoints().size(), "AccessPoints should be empty after removal.");
    }

}