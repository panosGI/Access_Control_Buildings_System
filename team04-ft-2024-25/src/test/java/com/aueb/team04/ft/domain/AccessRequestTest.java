package com.aueb.team04.ft.domain;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class AccessRequestTest {

    @Inject
    EntityManager em;

    private AccessRequest accessRequest;
    private Employee employee;
    private Building building;
    private String requestedAccessLevel;
    private Address address;

    @BeforeEach
    void setUp() {
        this.employee = new Employee("employee1", "password123", "employee1@example.com");
        this.address = new Address("12345", "Main St", "42");
        this.building = new Building("testName", "testBelongsTo", this.address);
        this.accessRequest = new AccessRequest(this.employee, this.building, "2");
    }

    @Test
    void defaultConstructor() {
        AccessRequest request = new AccessRequest();
        assertNull(request.getBuilding());
        assertNull(request.getRequestedAccessLevel());
    }

    @Test
    void constructorWithParameters() {
        this.requestedAccessLevel = "1";
        this.accessRequest = new AccessRequest(this.employee, this.building, this.requestedAccessLevel);
        assertEquals(this.employee, this.accessRequest.getUser());
        assertEquals(this.building, this.accessRequest.getBuilding());
        assertEquals(this.requestedAccessLevel, this.accessRequest.getRequestedAccessLevel());
    }

    @Transactional
    @Test
    void persistAccessRequest() {
        em.persist(this.employee);

        em.persist(this.building);
        em.persist(this.accessRequest);

        assertNotNull(this.accessRequest.getRequestID());
        assertNotNull(this.accessRequest.getTimestamp());
        assertEquals(RequestStatus.PENDING, this.accessRequest.getStatus());
        assertEquals("2", this.accessRequest.getRequestedAccessLevel());
    }

    @Test
    void getEmployee() {
        assertEquals(this.employee, this.accessRequest.getUser());
    }

    @Test
    void setEmployee() {
        Employee newEmployee = new Employee("newEmployee", "password123", "newEmployee@example.com");
        this.accessRequest.setUser(newEmployee);
        assertEquals(newEmployee, this.accessRequest.getUser());
    }

    @Test
    void getTimestamp() {
        assertNotNull(this.accessRequest.getTimestamp());
    }

    @Test
    void getStatus() {
        assertEquals(RequestStatus.PENDING, this.accessRequest.getStatus());
    }

    @Test
    void getBuilding() {
        assertEquals(this.building, this.accessRequest.getBuilding());
    }

    @Test
    void setTimestamp() {
        this.accessRequest.setTimestamp(this.accessRequest.getTimestamp().plusDays(1));
        assertNotNull(this.accessRequest.getTimestamp());
    }

    @Test
    void setBuilding() {
        Building newBuilding = new Building();
        this.accessRequest.setBuilding(newBuilding);
        assertEquals(newBuilding, this.accessRequest.getBuilding());
    }

    @Test
    void getRequestedAccessLevel() {
        assertEquals("2", this.accessRequest.getRequestedAccessLevel());
    }

    @Test
    void setRequestedAccessLevel() {
        this.accessRequest.setRequestedAccessLevel("3");
        assertEquals("3", this.accessRequest.getRequestedAccessLevel());
    }

    @Test
    void approve() {
        this.accessRequest.approve();
        assertEquals(RequestStatus.APPROVED, this.accessRequest.getStatus());
    }

    @Test
    void reject() {
        this.accessRequest.disapprove();
        assertEquals(RequestStatus.DISAPPROVED, this.accessRequest.getStatus());
    }

}
