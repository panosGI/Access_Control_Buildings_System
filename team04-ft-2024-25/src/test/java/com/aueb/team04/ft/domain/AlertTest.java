package com.aueb.team04.ft.domain;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class AlertTest {
    @Inject
    EntityManager em;

    @Test
    void testAlertDefaultConstructor() {
        Alert alert = new Alert();
        assertNotNull(alert);
    }
    @Test
    void testAlertConstructor() {
        Alert alert = new Alert("TestAlertType",0);
        assertNotNull(alert);
    }

    @Transactional
    @Test
    void persistAlert() {
        AccessLog accessLog = new AccessLog(LocalDateTime.now(), true);

        AccessPoint accessPoint = new AccessPoint();

        Address address = new Address("TestStreet123", "TestCity", "TestPostalCode");
        Building building = new Building();
        building.setBelongsTo("TestBelongsTo");
        building.setName("TestName123456789");
        building.setAddress(address);
        em.persist(building);

        Employee employee = new Employee("TestName007", "TestSurname", "TestEmail@example.com");
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

        Alert alert = new Alert("TestAlertType",0);
        alert.setAccessLog(accessLog);
        em.persist(alert);

        Alert retrievedAlert = em.find(Alert.class, alert.getId());
        assertNotNull(retrievedAlert);
        assertEquals(alert, retrievedAlert);
    }

    @Test
    void testGetId() {
        Alert alert = new Alert();
        assertNull(alert.getId());
    }


    @Test
    void testAlertConstructorNull() {
        Alert alert = new Alert(null, 0);
        assertNotNull(alert);
    }

    /*
    @Transactional
    @Test
    void testValidationOnAlertType() {
        Alert alert = new Alert("", 0); // Empty alertType should be invalid
        assertThrows(PersistenceException.class, () -> em.persist(alert));
    }

    @Transactional
    @Test
    void testValidationOnSeverity() {
        Alert alert = new Alert("ValidType", -1); // Negative severity should be invalid
        assertThrows(PersistenceException.class, () -> em.persist(alert));
    }

     */

    @Test
    void testGetters() {
        Alert alert1 = new Alert("TestAlertType",0);
        AccessLog accessLog = new AccessLog();
        alert1.setAccessLog(accessLog);

        assertEquals("TestAlertType",alert1.getAlertType());
        assertEquals(0,alert1.getSeverity());
        assertSame(accessLog,alert1.getAccessLog());
    }

    @Test
    void testSetters() {
        Alert alert1 = new Alert();
        alert1.setAlertType("TestAlertType");
        alert1.setSeverity(0);

        assertEquals("TestAlertType",alert1.getAlertType());
        assertEquals(0,alert1.getSeverity());
    }

    @Test
    void testLinkToAccessLog(){
        Alert alert1 = new Alert();
        AccessLog accessLog = new AccessLog();
        alert1.setAccessLog(accessLog);

        assertSame(accessLog,alert1.getAccessLog());
    }

    @Test
    void testToString() {
        Alert alert = new Alert("TestAlertType", 5);
        String expected = "Security Alert of severity level: 5 of type: TestAlertType";
        assertEquals(expected, alert.toString());
    }

}