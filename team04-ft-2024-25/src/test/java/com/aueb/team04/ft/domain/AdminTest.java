package com.aueb.team04.ft.domain;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the Admin entity.
 */
@QuarkusTest
public class AdminTest {

    @Inject
    EntityManager em;

    private Admin admin;
    private Request request;


    @BeforeEach
    void setUp() {
        this.admin = new Admin("admin1", "adminPass", "admin1@example.com");
        Employee employee = new Employee("employee1", "employeePass", "employee1@example.com");
        this.request = new Request(employee);
    }

    /**
     * Test the default constructor.
     */
    @Test
    void defaultConstructor() {
        Admin admin = new Admin();
        assertNotNull(admin);
    }

    /**
     * Test the constructor with parameters.
     */
    @Test
    void constructorWithParameters() {
        Admin admin2 = new Admin("admin2", "adminPass", "admin2@example.com");
        assertNotNull(admin2);
    }

    @Transactional
    @Test
    void persistAdmin() {
        this.em.persist(this.admin);

        Admin retrieved = this.em.find(Admin.class, this.admin.getId());

        assertNotNull(retrieved);
        assertEquals(this.admin, retrieved);

        this.em.remove(retrieved);
    }


    @Transactional
    @Test
    void testRequestAssociation() {
        // Persist the admin and request entities
        this.em.persist(this.admin);
        User employee = new Employee("employee001", "employeePass", "employee001@");
        this.request.setUser(employee);
        this.em.persist(employee);
        this.em.persist(this.request);

        // Associate the request with the admin
        this.admin.addModifiedRequest(this.request);
        this.em.merge(this.admin);

        // Use em.createQuery to find the request associated with the admin
        Admin retrievedAdmin = this.em.createQuery("SELECT a FROM Admin a WHERE a.id = :id", Admin.class)
                .setParameter("id", this.admin.getId())
                .getSingleResult();

        // Assert that the retrieved admin has the associated request
        assertNotNull(retrievedAdmin);
        assertTrue(retrievedAdmin.getModifiedRequests().contains(this.request));
    }

    @Test
    void addModifiedRequest() {
        this.admin.addModifiedRequest(this.request);
        assertTrue(this.admin.getModifiedRequests().contains(this.request));
    }

    @Test
    void getModifiedRequests() {
        this.admin.addModifiedRequest(this.request);
        Set<Request> requests = this.admin.getModifiedRequests();
        assertNotNull(requests);
    }

    @Test
    void setModifiedRequests() {
        Set<Request> requests = Set.of(this.request);
        this.admin.setModifiedRequests(requests);
        assertEquals(requests, this.admin.getModifiedRequests());
    }

    @Test
    void removeModifiedRequest() {
        this.admin.addModifiedRequest(this.request);
        this.admin.removeModifiedRequest(this.request);
        assertFalse(this.admin.getModifiedRequests().contains(this.request));
    }

}