package com.aueb.team04.ft.domain;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class RequestTest {
    @Inject
    EntityManager em;

    private Employee employee;
    private Admin admin;
    private Request request;

    @BeforeEach
    void setUp() {
        this.employee = new Employee("employee00", "password123", "employee1@example.com");
        this.admin = new Admin("admin11", "password123", "admin1@example.com");
        this.request = new Request(employee);
        this.request.setLastModifiedBy(admin);
    }

    @Test
    void defaultConstructor() {
        Request request2 = new Request();
        assertNull(request2.getUser());
    }

    @Test
    void constructorWithParameters() {
        assertEquals(this.employee, this.request.getUser());
    }

    @Transactional
    @Test
    void persistRequest() {
        em.persist(this.employee);
        em.persist(this.request);
        em.persist(this.admin);
        Request retrieved = em.find(Request.class, this.request.getRequestID());
        assertNotNull(retrieved);
        assertEquals(this.employee, retrieved.getUser());

    }

    @Transactional
    @Test
    void testEmployeeAssociation() {
        // Check if an employee with the same username already exists
        try {
            Employee existingEmployee = em.createQuery("SELECT e FROM Employee e WHERE e.username = :username", Employee.class)
                    .setParameter("username", this.employee.getUsername())
                    .getSingleResult();
            // If an employee with the same username exists, update the username to be unique
            this.employee.setUsername(this.employee.getUsername() + System.currentTimeMillis());
        } catch (NoResultException e) {
            // No employee with the same username exists, proceed with the original username
        }

        // Check if an admin with the same username already exists
        try {
            Admin existingAdmin = em.createQuery("SELECT a FROM Admin a WHERE a.username = :username", Admin.class)
                    .setParameter("username", this.admin.getUsername())
                    .getSingleResult();
            // If an admin with the same username exists, update the username to be unique
            this.admin.setUsername(this.admin.getUsername() + System.currentTimeMillis());
        } catch (NoResultException e) {
            // No admin with the same username exists, proceed with the original username
        }

        em.persist(this.employee);
        em.persist(this.admin);
        em.persist(this.request);
        Request retrieved = em.find(Request.class, this.request.getRequestID());
        assertNotNull(retrieved.getUser());
        assertEquals(this.employee, retrieved.getUser());
    }

    @Transactional
    @Test
    void testGetRequestID() {
        Request request1 = new Request();
        em.persist(request1);
        Request retrieved = em.find(Request.class, request1.getRequestID());
        assertNotNull(retrieved.getRequestID());
        assertEquals(request1.getRequestID(), retrieved.getRequestID());
    }

    @Test
    void testGetUser() {
        assertEquals(this.employee, this.request.getUser());
    }

    @Test
    void testGetTimestamp() {
        assertNotNull(this.request.getTimestamp());
    }

    @Test
    void testGetStatus() {
        assertEquals(RequestStatus.PENDING, this.request.getStatus());
    }

    @Test
    void testGetLastModifiedBy() {
        Request request1 = new Request();
        assertNull(request1.getLastModifiedBy());
    }

    @Test
    void testSetUser() {
        User newUser = new Employee("newUser", "password123", "newUser@example.com");
        this.request.setUser(newUser);
        assertEquals(newUser, this.request.getUser());
    }

    @Test
    void testSetTimestamp() {
        LocalDateTime newTimestamp = LocalDateTime.now().plusDays(1);
        this.request.setTimestamp(newTimestamp);
        assertEquals(newTimestamp, this.request.getTimestamp());
    }

    @Test
    void testSetStatus() {
        this.request.setStatus(RequestStatus.PENDING);
        assertEquals(RequestStatus.PENDING, this.request.getStatus());

        this.request.setStatus(RequestStatus.APPROVED);
        assertEquals(RequestStatus.APPROVED, this.request.getStatus());

        this.request.setStatus(RequestStatus.DISAPPROVED);
        assertEquals(RequestStatus.DISAPPROVED, this.request.getStatus());
    }    

    @Test
    void testApprove() {
        this.request.approve();
        assertEquals(RequestStatus.APPROVED, this.request.getStatus());
    }

    @Test
    void testDisapprove() {
        this.request.disapprove();
        assertEquals(RequestStatus.DISAPPROVED, this.request.getStatus());
    }

    @Test
    void testSetLastModifiedBy() {
        Request request1 = new Request();
        Admin admin1 = new Admin();
        request1.setLastModifiedBy(admin1);
        assertEquals(admin1, request1.getLastModifiedBy());
    }

    @Test
    void testSetLastModifiedByNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            this.request.setLastModifiedBy(null);
        });
        assertEquals("Admin cannot be null.", exception.getMessage());
    }

    @Test
    void testIsApproved() {
        assertFalse(this.request.isApproved());
        this.request.approve();
        assertTrue(this.request.isApproved());
    }

}