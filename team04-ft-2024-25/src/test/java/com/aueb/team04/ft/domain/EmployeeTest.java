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
class EmployeeTest {
    @Inject
    EntityManager em;

    private Employee employee;

    /**
     * Sets up the EntityManager before each test.
     */
    @BeforeEach
    void setUp() {
        employee = new Employee("employee13", "password123", "employee1@example.com");
    }


    /**
     * Tests that the Employee entity can be instantiated.
     */
    @Test
    void testEmployeeDefaultConstructor() {
        Employee employee = new Employee();
        assertNotNull(employee);
    }

    /**
     * Tests that the Employee entity can be instantiated with parameters.
     */
    @Test
    void testEmployeeConstructor() {
        Employee employee = new Employee("testUser", "password123", "test@example.com");
        assertNotNull(employee);
    }

    /**
     * Tests that the Employee entity can be instantiated with null parameters.
     */
    @Test
    void testEmployeeConstructorNull() {
        Employee employee = new Employee(null, null, null);
        assertNotNull(employee);
    }

    /**
     * Tests that the Get methods return the correct values.
     */
    @Test
    void testGetters() {
        assertEquals("employee13", employee.getUsername());
        assertEquals("password123", employee.getPassword());
        assertEquals("employee1@example.com", employee.getEmail());
        assertTrue(employee.getRequests().isEmpty()); // Initially, requests should be empty
        assertNull(employee.getAccessCard()); // Initially, access card should be null
    }

    /**
     * Tests that the Set methods set the correct values.
     */
    @Test
    void testSetters() {
        this.employee.setUsername("employee2");
        this.employee.setPassword("password456");
        this.employee.setEmail("employee2@example.com");

        assertEquals("employee2", this.employee.getUsername());
        assertEquals("password456", this.employee.getPassword());
        assertEquals("employee2@example.com", this.employee.getEmail());
    }

    @Test
    void testSetAccessCard() {
        AccessCard card = new AccessCard(LocalDateTime.now());
        this.employee.setAccessCard(card);
        assertEquals(card, this.employee.getAccessCard());
    }

    @Test
    void testAddRequest() {
        Request request = new Request();
        this.employee.addRequest(request);
        assertEquals(1, this.employee.getRequests().size());
        assertTrue(this.employee.getRequests().contains(request));
    }

    @Test
    void testRemoveRequestFromEmployee() {
        Request request = new Request();
        this.employee.addRequest(request);
        this.employee.getRequests().remove(request);
        assertFalse(this.employee.getRequests().contains(request));
        assertEquals(0, this.employee.getRequests().size());
    }

    @Test
    void setAccessCardToNull()  {
        this.employee.setAccessCard(null);
        assertNull(this.employee.getAccessCard());
    }

    @Test
    void replaceAccessCard() {
        AccessCard card1 = new AccessCard(LocalDateTime.now());
        AccessCard card2 = new AccessCard(LocalDateTime.now().plusDays(1));
        this.employee.setAccessCard(card1);
        this.employee.setAccessCard(card2);
        assertEquals(card2, this.employee.getAccessCard());
    }

    /**
     * Tests that an Employee entity can be persisted and retrieved from the database.
     */
    @Transactional
    @Test
    void testPersistEmployee() {
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

        em.persist(this.employee);
        Employee retrieved = em.find(Employee.class, this.employee.getId());
        assertNotNull(retrieved);
        assertEquals(this.employee.getUsername(), retrieved.getUsername());
    }

    /**
     * Tests that an AccessCard can be associated with an Employee and persisted.
     */
    @Transactional
    @Test
    void testAccessCardAssociation() {
        AccessCard card = new AccessCard(LocalDateTime.now());
        card.setEmployeeOfCard(this.employee);
        this.employee.setAccessCard(card);

        em.persist(this.employee);

        Employee retrieved = em.find(Employee.class, employee.getId());
        assertNotNull(retrieved.getAccessCard());
    }

    /**
     * Tests that a Request associated with an Employee is persisted correctly.
     */
    @Transactional
    @Test
    void testRequestCascade() {
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

        Request request = new Request();
        request.setUser(this.employee);
        this.employee.addRequest(request);
        em.persist(this.employee);
        Employee retrieved = em.find(Employee.class, this.employee.getId());
        assertNotNull(retrieved);
        assertEquals(1, retrieved.getRequests().size());
    }

    /**
     * Tests that an Employee entity can be updated.
     */
    @Transactional
    @Test
    void testUpdateEmployee() {
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

        em.persist(this.employee);

        Employee retrieved = em.find(Employee.class, this.employee.getId());
        retrieved.setPassword("newPassword123");
        em.merge(retrieved);

        Employee updated = em.find(Employee.class, this.employee.getId());
        assertEquals("newPassword123", updated.getPassword());
    }

    @Test
    void testSetAccessCardNull() {
        this.employee.setAccessCard(null); // Setting access card to null
        assertNull(this.employee.getAccessCard());
    }

}