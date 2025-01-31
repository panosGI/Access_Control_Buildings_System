package com.aueb.team04.ft.domain;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class AccessLogTest {
    @Inject
    EntityManager em;

    @Test
    void testAccessLogDefaultConstructor() {
        AccessLog accessLog = new AccessLog();
        assertNotNull(accessLog);
    }

    @Test
    void testAccessLogConstructor() {
        AccessLog accessLog = new AccessLog(LocalDateTime.now(),true);
        assertNotNull(accessLog);
    }

    @Test
    void testAccessLogConstructorNull() {
        AccessLog accessLog = new AccessLog(null,false);
        assertNotNull(accessLog);
    }

    @Transactional
    @Test
    void persistAccessLog() {
        AccessLog accessLog = new AccessLog(LocalDateTime.now(), true);

        AccessPoint accessPoint = new AccessPoint();

        Address address = new Address("TestStreet", "TestCity", "TestPostalCode");
        Building building = new Building();
        building.setBelongsTo("TestBelongsTo");
        building.setName("TestName");
        building.setAddress(address);
        em.persist(building);

        Employee employee = new Employee("TestName", "TestSurname", "TestEmail@example.com");
        em.persist(employee);

        AccessCard accessCard = new AccessCard();
        accessCard.setTimestamp(LocalDateTime.now());
        accessCard.setEmployeeOfCard(employee);
        em.persist(accessCard);

        accessPoint.setBuilding(building);
        accessPoint.setAccessLevelRequired("2");
        accessPoint.setType(AccessPointType.IN_BETWEEN);
        accessPoint.setLocation("TestLocation");
        em.persist(accessPoint);

        accessLog.setAccessCard(accessCard);
        accessLog.setAccessPoint(accessPoint);
        em.persist(accessLog);
        AccessLog retrievedAccessLog = em.find(AccessLog.class, accessLog.getId());
        assertNotNull(retrievedAccessLog);
        assertEquals(accessLog, retrievedAccessLog);
    }

    @Test
    void testGetters() {
        AccessLog accessLog = new AccessLog(LocalDateTime.of(1, 1, 1, 1, 1), true);

        // Testing if the getter methods return correct values.
        assertEquals(LocalDateTime.of(1, 1, 1, 1, 1), accessLog.getTimestamp());
        assertTrue(accessLog.getAccessStatus());
        assertNull(accessLog.getAccessCard());  // Should be null initially
        assertNull(accessLog.getAccessPoint());  // Should be null initially
        assertEquals(new HashSet<>(), accessLog.getAlerts());  // Should return an empty set
    }

    @Test
    void testSetters() {
        AccessLog accessLog = new AccessLog();
        accessLog.setAccessStatus(true);
        accessLog.setTimestamp(LocalDateTime.of(1, 1, 1, 1, 1));

        AccessPoint accessPoint = new AccessPoint();
        accessLog.setAccessPoint(accessPoint);

        AccessCard accessCard = new AccessCard();
        accessLog.setAccessCard(accessCard);

        // Verifying that the setters correctly updated the values.
        assertTrue(accessLog.getAccessStatus());
        assertEquals(LocalDateTime.of(1, 1, 1, 1, 1), accessLog.getTimestamp());
        assertEquals(accessPoint, accessLog.getAccessPoint());
        assertEquals(accessCard, accessLog.getAccessCard());

        // Now testing with null values for AccessCard and AccessPoint
        accessLog.setAccessCard(null);
        accessLog.setAccessPoint(null);

        // Verifying that null values are correctly set.
        assertNull(accessLog.getAccessCard());
        assertNull(accessLog.getAccessPoint());
    }

    @Test
    void testAddAlert() {
        AccessLog accessLog = new AccessLog();
        Alert alert = new Alert();
        accessLog.addAlert(alert);

        Set<Alert> alertsTest = new HashSet<>();
        alertsTest.add(alert);

        assertEquals(alertsTest,accessLog.getAlerts());
    }

    @Test
    void testTriggers() {
        AccessLog accessLog = new AccessLog();
        accessLog.triggers("TestType", 0);  // Calls triggers to add an alert.

        // Retrieve the alert from the AccessLog's alerts set.
        Alert triggeredAlert = accessLog.getAlerts().iterator().next();

        // Verifying that the alert properties are set correctly.
        assertEquals("TestType", triggeredAlert.getAlertType());
        assertEquals(0, triggeredAlert.getSeverity());
    }

}