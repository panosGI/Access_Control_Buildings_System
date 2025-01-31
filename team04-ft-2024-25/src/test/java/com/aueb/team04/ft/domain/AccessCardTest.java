package com.aueb.team04.ft.domain;

import com.aueb.team04.ft.persistence.AccessLogRepository;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import jakarta.persistence.EntityManager;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@QuarkusTest
public class AccessCardTest {

    @Inject
    EntityManager em;
    Set<AccessPermission> accessPermissions;

    @Inject
    AccessLogRepository accessLogRepository;


    @Test
    void testAccessCardDefaultConstructor() {
        AccessCard accessCard = new AccessCard();
        assertNotNull(accessCard);
    }

    @Test
    void testAccessCardWithTimestamp() {
        LocalDateTime timestamp = LocalDateTime.now();
        AccessCard accessCard = new AccessCard(timestamp);
        assertNotNull(accessCard);
        assertEquals(timestamp, accessCard.getTimestamp());
    }

    /*
        * Test persisting an AccessCard entity to the database
     */
    @Transactional
    @Test
    void testPersistAccessCard() {
        // Find the employee from the database

        Employee employee = em.createQuery("SELECT user FROM User user WHERE user.id = 4444", Employee.class).getSingleResult();

        // Create a new AccessCard and set its properties
        AccessCard accessCard = new AccessCard(LocalDateTime.now());
        accessCard.setEmployeeOfCard(employee);
        accessCard.setAccessLogs(new HashSet<>());
        accessCard.setAccessPermissions(new HashSet<>());

        // Persist the AccessCard
        em.persist(accessCard);

        // Retrieve the AccessCard from the database
        AccessCard retrieved = em.find(AccessCard.class, accessCard.getId());
        assertNotNull(retrieved, "AccessCard should be persisted and retrievable from the database");
        assertEquals(accessCard, retrieved, "Persisted AccessCard should match the retrieved AccessCard");
    }

    @Test
    public void testSettersAndGetters() {
        AccessCard accessCard = new AccessCard();
        LocalDateTime timestamp = LocalDateTime.now();
        accessCard.setTimestamp(timestamp);

        assertEquals(timestamp, accessCard.getTimestamp());
    }

    @Test
    public void testSetAndGetEmployee() {
        AccessCard accessCard = new AccessCard();
        Employee employee = new Employee("username", "password", "email");
        accessCard.setEmployeeOfCard(employee);

        assertEquals(employee, accessCard.getEmployee());
    }

    @Test
    public void testSetAndGetAccessPermissions() {
        AccessPermission accessPermission = new AccessPermission();
        accessPermission.setAccessLevel("3");

        AccessCard accessCard = new AccessCard();
        accessCard.setAccessPermissions(accessPermissions);

        assertEquals(accessPermissions, accessCard.getAccessPermissions());
    }

    @Test
    public void testSetAndGetAccessLogs() {
        AccessLog accessLog1 = new AccessLog();
        AccessLog accessLog2 = new AccessLog();
        accessLog1.setAccessStatus(true);
        accessLog2.setAccessStatus(false);
        Set<AccessLog> accessLogs = new HashSet<>();
        accessLogs.add(accessLog1);
        accessLogs.add(accessLog2);

        AccessCard accessCard = new AccessCard();
        accessCard.setAccessLogs(accessLogs);
        assertEquals(accessLogs, accessCard.getAccessLogs(), "AccessPoints should match the set values.");
        assertEquals(2, accessCard.getAccessLogs().size(), "AccessPoints should contain exactly 2 items.");
    }

    @Transactional
    @Test
    void testEmployeeAssociation() {
        Employee employee = new Employee("username", "password", "email");

        AccessCard accessCard = new AccessCard(LocalDateTime.now());
        accessCard.setEmployeeOfCard(employee);

        em.persist(employee);
        em.persist(accessCard);

        AccessCard retrieved = em.find(AccessCard.class, accessCard.getId());
        assertNotNull(retrieved.getEmployee());
    }

    @Transactional
    @Test
    void testAccessLogAssociation() {
        AccessCard accessCard = new AccessCard(LocalDateTime.now());
        AccessLog accessLog = new AccessLog();
        accessLog.setAccessStatus(true);
        accessLog.setAccessCard(accessCard);
        accessCard.addAccessLog(accessLog);

        em.persist(accessCard);
        em.persist(accessLog);

        AccessCard retrieved = em.find(AccessCard.class, accessCard.getId());
        assertNotNull(retrieved.getAccessLogs());
        assertEquals(1, retrieved.getAccessLogs().size());
    }


    @Test
    public void testAddAccessPermission() {
        AccessPermission accessPermission = new AccessPermission();
        AccessCard accessCard = new AccessCard();
        accessCard.addPermission(accessPermission);

        assertTrue(accessCard.getAccessPermissions().contains(accessPermission), "AccessLogs should contain the added permission");
        assertEquals(1, accessCard.getAccessPermissions().size(), "AccessLogs should contain exactly 1 item");
    }

    @Test
    public void testRemoveAccessPermission() {
        AccessPermission accessPermission = new AccessPermission();
        AccessCard accessCard = new AccessCard();
        accessCard.addPermission(accessPermission);
        accessCard.removePermission(accessPermission);

        assertFalse(accessCard.getAccessPermissions().contains(accessPermission), "AccessLogs should not contain the removed permission");
        assertEquals(0, accessCard.getAccessPermissions().size(), "AccessLogs should be empty");
    }

    @Test
    public void testAddAccessLog() {
        AccessLog log = new AccessLog();
        AccessCard accessCard = new AccessCard();
        accessCard.addAccessLog(log);

        assertTrue(accessCard.getAccessLogs().contains(log), "AccessLogs should contain the added log");
        assertEquals(1, accessCard.getAccessLogs().size(), "AccessLogs should contain exactly 1 item");
    }

    @Test
    public void testRemoveAccessLog() {
        AccessLog log = new AccessLog();
        AccessCard accessCard = new AccessCard();
        accessCard.addAccessLog(log);
        accessCard.removeAccessLog(log);

        assertFalse(accessCard.getAccessLogs().contains(log), "AccessLogs should not contain the removed log");
        assertEquals(0, accessCard.getAccessLogs().size(), "AccessLogs should be empty after removal");
    }

    // Test case where access is granted
    @Test
    public void testCheckAccessGranted() {
        Building building = new Building();
        building.setName("Building A");

        AccessPoint accessPoint = new AccessPoint();
        accessPoint.setLocation("Main Entrance");
        accessPoint.setType(AccessPointType.INPUT);
        accessPoint.setAccessLevelRequired("2");

        AccessPermission permission = new AccessPermission();
        permission.setBuilding(building);
        permission.setAccessLevel("3");

        AccessCard accessCard = new AccessCard();
        accessCard.addPermission(permission);

        assertTrue(accessCard.checkAccess(accessPoint), "Access should be granted");
    }


    // Test case where access is not granted due to insufficient access level
    @Test
    public void testCheckAccessInsufficientAccessLevel() {
        Building building = new Building();
        building.setName("Building A");

        AccessPoint accessPoint = new AccessPoint();
        accessPoint.setLocation("Main Entrance");
        accessPoint.setType(AccessPointType.INPUT);
        accessPoint.setAccessLevelRequired("4");

        AccessPermission permission = new AccessPermission();
        permission.setBuilding(building);
        permission.setAccessLevel("3");

        AccessCard accessCard = new AccessCard();
        accessCard.addPermission(permission);

        assertFalse(accessCard.checkAccess(accessPoint), "Access should not be granted due to insufficient access level");
    }

    // Test case where access is not granted due to no access permissions
    @Test
    public void testCheckAccessNoPermissions() {
        Building building = new Building();
        building.setName("Building A");

        AccessPoint accessPoint = new AccessPoint();
        accessPoint.setLocation("Main Entrance");
        accessPoint.setType(AccessPointType.INPUT);
        accessPoint.setAccessLevelRequired("2");

        AccessCard noPermissionsCard = new AccessCard();

        assertFalse(noPermissionsCard.checkAccess(accessPoint), "Access should not be granted when there are no access permissions");
    }

    // Test case where an AccessPoint has a type of "Entry" or "Exit"
    @Test
    public void testHasAccessToPrerequisitesThrowsException() {
        Building building = new Building();
        building.setName("Building A");

        AccessPoint accessPoint1 = new AccessPoint();
        accessPoint1.setLocation("Main Entrance");
        accessPoint1.setType(AccessPointType.INPUT);
        accessPoint1.setAccessLevelRequired("1");
        accessPoint1.setBuilding(building);

        AccessPoint accessPoint2 = new AccessPoint();
        accessPoint2.setLocation("Side Entrance");
        accessPoint2.setType(AccessPointType.OUTPUT);
        accessPoint2.setAccessLevelRequired("1");
        accessPoint2.setBuilding(building);

        AccessPermission permission = new AccessPermission();
        permission.setBuilding(building);
        permission.setAccessLevel("2");

        AccessCard accessCard = new AccessCard();
        accessCard.addPermission(permission);

        Set<AccessPoint> prerequisites = new HashSet<>();
        prerequisites.add(accessPoint1);
        prerequisites.add(accessPoint2);

        assertThrows(IllegalArgumentException.class, () -> accessCard.hasAccessToPrerequisites(prerequisites), "Should throw IllegalArgumentException for AccessPoint with type 'Entry' or 'Exit'");
    }

    // Test case where access is granted to at least one prerequisite
    @Test
    public void testHasAccessToPrerequisitesGranted() {
        Building building = new Building();
        building.setName("Building A");

        AccessPoint accessPoint1 = new AccessPoint();
        accessPoint1.setLocation("Main Entrance");
        accessPoint1.setType(AccessPointType.IN_BETWEEN);
        accessPoint1.setAccessLevelRequired("1");
        accessPoint1.setBuilding(building);

        AccessPoint accessPoint2 = new AccessPoint();
        accessPoint2.setLocation("Side Entrance");
        accessPoint2.setType(AccessPointType.IN_BETWEEN);
        accessPoint2.setAccessLevelRequired("1");
        accessPoint2.setBuilding(building);

        AccessPermission permission = new AccessPermission();
        permission.setBuilding(building);
        permission.setAccessLevel("2");

        AccessCard accessCard = new AccessCard();
        accessCard.addPermission(permission);

        Set<AccessPoint> prerequisites = new HashSet<>();
        prerequisites.add(accessPoint1);
        prerequisites.add(accessPoint2);

        assertTrue(accessCard.hasAccessToPrerequisites(prerequisites), "Access should be granted to at least one prerequisite");
    }

    // Test case where access is not granted to any prerequisites
    @Test
    public void testHasAccessToPrerequisitesNotGranted() {
        Building building = new Building();
        building.setName("Building A");

        AccessPoint accessPoint1 = new AccessPoint();
        accessPoint1.setLocation("Main Entrance");
        accessPoint1.setType(AccessPointType.IN_BETWEEN);
        accessPoint1.setAccessLevelRequired("3");
        accessPoint1.setBuilding(building);

        AccessPoint accessPoint2 = new AccessPoint();
        accessPoint2.setLocation("Side Entrance");
        accessPoint2.setType(AccessPointType.IN_BETWEEN);
        accessPoint2.setAccessLevelRequired("3");
        accessPoint2.setBuilding(building);

        AccessPermission permission = new AccessPermission();
        permission.setBuilding(building);
        permission.setAccessLevel("2");

        AccessCard accessCard = new AccessCard();
        accessCard.addPermission(permission);

        Set<AccessPoint> prerequisites = new HashSet<>();
        prerequisites.add(accessPoint1);
        prerequisites.add(accessPoint2);

        assertFalse(accessCard.hasAccessToPrerequisites(prerequisites), "Access should not be granted to any prerequisites");
    }

    @Test
    public void testEmptyPrerequisites() {
        Building building = new Building();
        building.setName("Building A");

        AccessPoint accessPoint1 = new AccessPoint();
        accessPoint1.setLocation("Main Entrance");
        accessPoint1.setType(AccessPointType.IN_BETWEEN);
        accessPoint1.setAccessLevelRequired("3");
        accessPoint1.setBuilding(building);

        AccessPermission permission = new AccessPermission();
        permission.setBuilding(building);
        permission.setAccessLevel("2");

        AccessCard accessCard = new AccessCard();
        accessCard.addPermission(permission);

        Set<AccessPoint> prerequisites = new HashSet<>();

        assertTrue(accessCard.hasAccessToPrerequisites(prerequisites), "Access should not be granted to any prerequisites");
    }

    // Test case where the AccessCard has the required access permission for the specified building
    @Test
    public void testGetAccessPermissionForBuildingExists() {
        Building building1 = new Building();
        building1.setName("Building A");

        AccessPermission permission1 = new AccessPermission();
        permission1.setBuilding(building1);
        permission1.setAccessLevel("3");

        AccessCard accessCard = new AccessCard();
        accessCard.addPermission(permission1);

        assertEquals(permission1, accessCard.getAccessPermissionForBuilding(building1), "Should return the correct AccessPermission for Building A");
    }

    // Test case where the AccessCard does not have the required access permission for the specified building
    @Test
    public void testGetAccessPermissionForBuildingNotExists() {
        Building building1 = new Building();
        building1.setName("Building A");

        Building building2 = new Building();
        building2.setName("Building B");

        AccessPermission permission1 = new AccessPermission();
        permission1.setBuilding(building1);
        permission1.setAccessLevel("3");

        AccessCard accessCard = new AccessCard();
        accessCard.addPermission(permission1);

        assertNull(accessCard.getAccessPermissionForBuilding(building2), "Should return null for a building with no access permission");
    }

    // Test case where the AccessCard has multiple access permissions, including one for the specified building
    @Test
    public void testGetAccessPermissionForBuildingMultiplePermissions() {
        Building building1 = new Building();
        building1.setName("Building A");

        Building building2 = new Building();
        building2.setName("Building B");

        AccessPermission permission1 = new AccessPermission();
        permission1.setBuilding(building1);
        permission1.setAccessLevel("3");

        AccessPermission permission2 = new AccessPermission();
        permission2.setBuilding(building2);
        permission2.setAccessLevel("2");

        AccessCard accessCard = new AccessCard();
        accessCard.addPermission(permission1);
        accessCard.addPermission(permission2);

        assertEquals(permission2, accessCard.getAccessPermissionForBuilding(building2), "Should return the correct AccessPermission for Building B");
    }    

}