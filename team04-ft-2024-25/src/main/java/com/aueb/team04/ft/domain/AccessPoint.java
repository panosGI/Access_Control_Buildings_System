package com.aueb.team04.ft.domain;

import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "access_point")
@JsonIgnoreProperties({"prerequisites"})
public class AccessPoint {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name="location")
    private String location;

    @Enumerated(EnumType.STRING)
    @Column(name="type")
    private AccessPointType type;

    @Column(name = "access_level_required")
    private String accessLevelRequired;

    @JsonIgnore
    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JoinTable(
            name = "access_point_prerequisites",
            joinColumns = @JoinColumn(name = "source_access_point_id"),
            inverseJoinColumns = @JoinColumn(name = "target_access_point_id")
    )
    private Set<AccessPoint> prerequisites = new HashSet<>();

    @JsonbTransient
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "building_id")
    private Building building;

    @JsonIgnore
    @OneToMany(mappedBy = "accessPoint", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<AccessLog> accessLogs = new HashSet<>();

    public AccessPoint() {}

    // Getters and setters
    public Long getId() {
        return id;
    }

    public String getLocation() {
        return location;
    }   
    public void setLocation(String location) {
        this.location = location;
    }

    public AccessPointType getType() {
        return type;
    }

    public void setType(AccessPointType type) {
        this.type = type;
    }

    public String getAccessLevelRequired() {
        return accessLevelRequired;
    }

    public void setAccessLevelRequired(String accessLevelRequired) {
        this.accessLevelRequired = accessLevelRequired;
    }

    public Set<AccessPoint> getPrerequisites() {
        return prerequisites;
    }

    public void setPrerequisites(Set<AccessPoint> prerequisites) {
        this.prerequisites = prerequisites;
    }

    public Building getBuilding() {
        return building;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }

    public Set<AccessLog> getAccessLogs() {
        return accessLogs;
    }

    public void setAccessLogs(Set<AccessLog> accessLogs) {
        this.accessLogs = accessLogs;
    }

    // Add and remove methods for prerequisites
    public void addPrerequisite(AccessPoint prerequisite) {
        this.prerequisites.add(prerequisite);
    }

    public void removePrerequisite(AccessPoint prerequisite) {
        this.prerequisites.remove(prerequisite);
    }

    // Add and remove methods for accessLogs
    public void addAccessLog(AccessLog accessLog) {
        this.accessLogs.add(accessLog);
    }

    public void removeAccessLog(AccessLog accessLog) {
        this.accessLogs.remove(accessLog);
    }


}