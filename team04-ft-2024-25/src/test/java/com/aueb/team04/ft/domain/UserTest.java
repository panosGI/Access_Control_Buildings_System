package com.aueb.team04.ft.domain;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


@QuarkusTest
public class UserTest {
    @Inject
    EntityManager entityManager;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User("user1", "user1", "user1@example.com");
    }

    @Test
    void testDefaultConstructor() {
        User user2 = new User();
        assertNotNull(user2);
    }

    @Test
    void testUserConstructorWithParameters() {
        assertNotNull(this.user);
    }

    @Transactional
    @Test
    void persistUser() {
        entityManager.persist(this.user);
        User retrieved = entityManager.find(User.class, this.user.getId());
        assertEquals(this.user, retrieved);
    }

    @Transactional
    @Test
    void testRequestAssociation() {
        // Check if a user with the same username already exists
        try {
            User existingUser = entityManager.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                    .setParameter("username", this.user.getUsername())
                    .getSingleResult();
            // If a user with the same username exists, update the username to be unique
            this.user.setUsername(this.user.getUsername() + System.currentTimeMillis());
        } catch (NoResultException e) {
            // No user with the same username exists, proceed with the original username
        }

        Request request = new Request();
        this.user.addRequest(request);

        entityManager.persist(this.user);
        entityManager.persist(request);

        User retrieved = entityManager.find(User.class, this.user.getId());
        assertEquals(request, retrieved.getRequests().iterator().next());
    }

    @Test
    void testGetId() {
        assertEquals(0, this.user.getId());
    }

    @Test
    void testGetUsername() {
        assertEquals("user1", this.user.getUsername());
    }

    @Test
    void testGetPassword() {
        assertEquals("user1", this.user.getPassword());
    }

    @Test
    void testGetEmail() {
        assertEquals("user1@example.com", this.user.getEmail());
    }

    @Test
    void testSetUsername() {
        this.user.setUsername("newTest");
        assertEquals("newTest", this.user.getUsername());
    }

    @Test
    void testSetPassword() {
        this.user.setPassword("newTestPassword");
        assertEquals("newTestPassword", this.user.getPassword());
    }

    @Test
    void testSetEmail() {
        this.user.setEmail("newTestEmail@example.com");
        assertEquals("newTestEmail@example.com", this.user.getEmail());
    }

    @Test
    void testGetRequests() {
        Request request = new Request();
        this.user.addRequest(request);
        assertEquals(request, this.user.getRequests().iterator().next());
    }

    @Test
    void testSetRequests() {
        Request request = new Request();
        Request request1 = new Request();

        Set<Request> requests = new HashSet<>();
        requests.add(request);
        requests.add(request1);

        this.user.setRequests(requests);
        assertEquals(requests, this.user.getRequests());
    }

    @Test
    void testAddRequest() {
        Request request = new Request();
        this.user.addRequest(request);
        assertEquals(request, this.user.getRequests().iterator().next());
    }

}