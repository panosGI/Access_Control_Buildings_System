package com.aueb.team04.ft.domain;

import jakarta.json.bind.annotation.JsonbDateFormat;
import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Entity representing an Access Card.
 */
@Entity
@Table(name = "access_card")
public class AccessCard {

    /**
     * Unique identifier for the Access Card.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * Timestamp indicating when the Access Card was created.
     */
    @JsonbDateFormat("yyyy-MM-dd HH:mm:ss")
    @Column(name="cardTimestamp")
    private LocalDateTime timestamp;

    /**
     * The employee associated with this Access Card.
     */
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name="employeeId", unique = true)
    private Employee employee;

    /**
     * Set of access permissions associated with this Access Card.
     */
    @OneToMany(mappedBy = "accessCard", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<AccessPermission> accessPermissions = new HashSet<>();

    /**
     * Set of access logs associated with this Access Card.
     */
    @JsonbTransient
    @OneToMany(mappedBy = "accessCard", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<AccessLog> accessLogs = new HashSet<>();

    /**
     * Default constructor.
     */
    public AccessCard() {}

    /**
     * Constructor with timestamp.
     *
     * @param timestamp the timestamp when the Access Card was created
     */
    public AccessCard(LocalDateTime timestamp){
        this.timestamp = timestamp;
    }

    /**
     * Gets the unique identifier for this Access Card.
     *
     * @return the unique identifier
     */
    public Long getId(){
        return this.id;
    }

    /**
     * Gets the timestamp when this Access Card was created.
     *
     * @return the timestamp
     */
    public LocalDateTime getTimestamp(){
        return this.timestamp;
    }

    /**
     * Sets the timestamp when this Access Card was created.
     *
     * @param timestamp the timestamp to set
     */
    public void setTimestamp(LocalDateTime timestamp){
        this.timestamp=timestamp;
    }

    /**
     * Gets the employee associated with this Access Card.
     *
     * @return the employee
     */
    public Employee getEmployee() {
        return this.employee;
    }

    /**
     * Links this Access Card to an employee.
     *
     * @param employee the employee to link
     */
    public void setEmployeeOfCard(Employee employee) {
        this.employee = employee;
    }

    /**
     * Gets the set of access permissions associated with this Access Card.
     *
     * @return the set of access permissions
     */
    public Set<AccessPermission> getAccessPermissions() {
        return accessPermissions;
    }

    /**
     * Sets the set of access permissions associated with this Access Card.
     *
     * @param accessPermissions the set of access permissions to set
     */
    public void setAccessPermissions(Set<AccessPermission> accessPermissions) {
        this.accessPermissions = accessPermissions;
    }

    /**
     * Gets the set of access logs associated with this Access Card.
     *
     * @return the set of access logs
     */
    public Set<AccessLog> getAccessLogs(){
        return accessLogs;
    }

    /**
     * Sets the set of access logs associated with this Access Card.
     *
     * @param accessLogs the set of access permissions to set
     */
    public void setAccessLogs(Set<AccessLog> accessLogs) {
        this.accessLogs = accessLogs;
    }

    /**
     * Adds an access permission to this Access Card.
     *
     * @param permission the access permission to add
     */
    public void addPermission(AccessPermission permission) {
        accessPermissions.add(permission);
    }

    /**
     * Removes an access permission from this Access Card.
     *
     * @param accessPermission the access permission to remove
     * @return true if the permission was removed, false otherwise
     */
    public boolean removePermission(AccessPermission accessPermission) {
        return accessPermissions.remove(accessPermission);
    }

    /**
     * Adds an access log to this Access Card.
     *
     * @param accessLog the access log to add
     */
    public void addAccessLog(AccessLog accessLog){
        accessLogs.add(accessLog);
    }

    /**
     * Removes an access log from this Access Card.
     *
     * @param accessLog the access log to remove
     */
    public void removeAccessLog(AccessLog accessLog){
        accessLogs.remove(accessLog);
    }

    /**
     * Checks if this Access Card has access to a specific building and access point.
     *
     * @param accessPoint the access point to check access for
     * @return true if access is granted, false otherwise
     */
    public Boolean checkAccess(AccessPoint accessPoint) {
        for (AccessPermission ap : accessPermissions) {
            if (Integer.parseInt(ap.getAccessLevel()) >= Integer.parseInt(accessPoint.getAccessLevelRequired())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if this Access Card has access to at least one of the prerequisite Access Points.
     * It is forbidden for Access Points to have a type of "Entry" and "Exit".
     *
     * @param prerequisites the set of Access Points to check access for
     * @return true if the Access Card has access to at least one of the prerequisite Access Points, false otherwise
     */
    public boolean hasAccessToPrerequisites(Set<AccessPoint> prerequisites) {
        if (prerequisites.isEmpty()) {
            System.out.println("No prerequisites");
            return true;
        }
        for (AccessPoint accessPoint : prerequisites) {
            if (accessPoint.getType().equals(AccessPointType.INPUT) || accessPoint.getType().equals(AccessPointType.OUTPUT)) {
                throw new IllegalArgumentException("Prerequisite Access Points cannot have a type of 'Entry' or 'Exit'");
            }
            if (checkAccess(accessPoint)) {
                System.out.println("Access granted to prerequisite");
                return true;
            }
        }
        System.out.println("Access denied to all prerequisites");
        return false;
    }

    /**
     * Gets the access permission for a specific building.
     *
     * @param building the building to get the access permission for
     * @return the access permission for the building, or null if not found
     */

    public AccessPermission getAccessPermissionForBuilding(Building building){
        AccessPermission PermissionReferringToBuilding = null;
        for (AccessPermission ap : this.accessPermissions){
            if (ap.getBuilding().equals(building)){
                PermissionReferringToBuilding=ap;
            }
        }
        return PermissionReferringToBuilding;
    }
}