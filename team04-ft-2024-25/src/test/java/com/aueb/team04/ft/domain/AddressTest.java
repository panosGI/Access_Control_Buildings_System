package com.aueb.team04.ft.domain;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import jakarta.persistence.EntityManager;
import static org.junit.jupiter.api.Assertions.*;


@QuarkusTest
public class AddressTest {
    @Inject
    EntityManager em;

    @Test
    public void testDefaultConstructor() {
        Address address = new Address();
        assertNotNull(address);
    }

    @Test
    public void testConstructor() {
        Address address = new Address("12345", "Main St", "42");
        assertNotNull(address);
    }

    @Transactional
    @Test
    public void testPersistAddress() {

        Address address = new Address("12345", "Main St", "42");
        Building building = new Building();
        building.setName("Building TEST");
        building.setBelongsTo("Owner A");
        building.setAddress(address);
        em.persist(building);

        Building retrievedBuilding = em.find(Building.class, building.getId());
        assertNotNull(retrievedBuilding, "Building should be persisted and retrieved successfully");

        Address retrievedAddress = retrievedBuilding.getAddress();
        assertNotNull(retrievedAddress, "Address should be embedded and retrieved successfully");
        assertEquals("12345", retrievedAddress.getZipcode(), "Zipcode should match the persisted value");
        assertEquals("Main St", retrievedAddress.getStreetName(), "Street name should match the persisted value");
        assertEquals("42", retrievedAddress.getStreetNumber(), "Street number should match the persisted value");

    }
    
}