package com.aueb.team04.ft.domain;

import java.util.HashSet;
import java.util.Set;

import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.*;

@Entity
@Table(name = "building")
public class Building {
    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="id", unique = true, nullable = false)
    private Long id;

    @Column(name="name", unique = true)
    private String name;

    @Column(name = "belongs_to")
    private String belongsTo;

    @Embedded
    private Address address;

    @JsonbTransient
    @OneToMany(mappedBy = "building", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<AccessPermission> accessPermissions = new HashSet<>();

    @JsonbTransient
    @OneToMany(mappedBy = "building", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<AccessPoint> accessPoints = new HashSet<>();

    public Building() {}

    public Building(String name, String belongsTo, Address address) {
        this.name = name;
        this.belongsTo = belongsTo;
        this.address = address;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBelongsTo() {
        return belongsTo;
    }

    public void setBelongsTo(String belongsTo) {
        this.belongsTo = belongsTo;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Set<AccessPermission> getAccessPermissions() {
        return accessPermissions;
    }

    public void setAccessPermissions(Set<AccessPermission> accessPermissions) {
        this.accessPermissions = accessPermissions;
    }

    public Set<AccessPoint> getAccessPoints() {
        return accessPoints;
    }

    public void setAccessPoints(Set<AccessPoint> accessPoints) {
        this.accessPoints = accessPoints;
    }

    // Add and Remove methods for accessPermissions
    public void addAccessPermission(AccessPermission accessPermission) {
        this.accessPermissions.add(accessPermission);
    }

    public void removeAccessPermission(AccessPermission accessPermission) {
        this.accessPermissions.remove(accessPermission);
    }

    // Add and Remove methods for accessPoints
    public void addAccessPoint(AccessPoint accessPoint) {
        this.accessPoints.add(accessPoint);
    }

    public void removeAccessPoint(AccessPoint accessPoint) {
        this.accessPoints.remove(accessPoint);
    }
}