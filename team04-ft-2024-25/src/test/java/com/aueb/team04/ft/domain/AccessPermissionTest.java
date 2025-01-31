package com.aueb.team04.ft.domain;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import jakarta.persistence.EntityManager;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;


@QuarkusTest
public class AccessPermissionTest {
    @Inject
    EntityManager em;

    private AccessCard accessCard;
    private Building building;

    @Test
    void testAccessPermissionDefaultConstructor() {
        AccessPermission accessPermission = new AccessPermission();
        assertNotNull(accessPermission);
    }

    @Transactional
    @Test
    void testPersistAccessPermission() {
        AccessPermission accessPermission = new AccessPermission();
        accessPermission.setAccessLevel("3");
        em.persist(accessPermission);

        AccessPermission retrieved = em.find(AccessPermission.class, accessPermission.getId());
        assertNotNull(retrieved);
        assertEquals("3", retrieved.getAccessLevel());
    }

    @Test
    public void testSettersAndGetters() {
        AccessPermission accessPermission = new AccessPermission();
        accessPermission.setAccessLevel("3");

        assertEquals("3", accessPermission.getAccessLevel());
    }

    @Test
    public void testSetAndGetAccessCard() {
        AccessPermission accessPermission = new AccessPermission();
        accessPermission.setAccessCard(accessCard);
        assertEquals(accessCard, accessPermission.getAccessCard(), "AccessPermission should match the set values.");
    }

    @Test
    public void testSetAndGetBuilding() {
        AccessPermission accessPermission = new AccessPermission();
        accessPermission.setBuilding(building);
        assertEquals(building, accessPermission.getBuilding(), "Building should match the set value");
    }

    @Transactional
    @Test
    void testBuildingAssociation() {
        Address address = new Address();
        address.setZipcode("12345");
        address.setStreetName("Main St");
        address.setStreetNumber("12");

        Building building = new Building();
        building.setName("Building A");
        building.setBelongsTo("Owner A");
        building.setAddress(address);

        AccessPermission accessPermission = new AccessPermission();
        accessPermission.setAccessLevel("3");
        accessPermission.setBuilding(building);

        em.persist(building);
        em.persist(accessPermission);

        AccessPermission retrieved = em.find(AccessPermission.class, accessPermission.getId());
        assertNotNull(retrieved, "AccessPermission should be persisted and retrieved successfully");
        assertNotNull(retrieved.getBuilding(), "Building should be associated and retrieved successfully");
        assertEquals("Building A", retrieved.getBuilding().getName(), "Building name should match the persisted value");
        assertEquals("Owner A", retrieved.getBuilding().getBelongsTo(), "Building owner should match the persisted value");
    }

    @Transactional
    @Test
    void testAccessCardAssociation() {
        AccessPermission accessPermission = new AccessPermission();
        accessPermission.setAccessLevel("3");

        AccessCard card = new AccessCard(LocalDateTime.now());
        Employee employee = new Employee();
        card.setEmployeeOfCard(employee);
        em.persist(employee);
        em.persist(card); // Persist the AccessCard first

        accessPermission.setAccessCard(card); // Associate the persisted AccessCard
        em.persist(accessPermission); // Now persist AccessPermission

        AccessPermission retrieved = em.find(AccessPermission.class, accessPermission.getId());
        assertNotNull(retrieved.getAccessCard());
    }

}