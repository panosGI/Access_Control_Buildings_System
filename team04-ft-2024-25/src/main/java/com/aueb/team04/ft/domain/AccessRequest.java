package com.aueb.team04.ft.domain;

import jakarta.persistence.*;

/**
 * Entity representing an Access Request.
 */
@Entity
@DiscriminatorValue("ACCESS_REQUEST")
public class AccessRequest extends Request {

    /**
     * The building for which access is requested.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "building_id")
    private Building building;

    /**
     * The requested access level.
     */
    private String requestedAccessLevel;

    /**
     * Default constructor.
     */
    public AccessRequest() {
    }

    /**
     * Constructor with parameters.
     *
     * @param requester            the employee requesting access
     * @param building             the building for which access is requested
     * @param requestedAccessLevel the requested access level
     */
    public AccessRequest(Employee requester, Building building, String requestedAccessLevel) {
        super(requester);
        this.building = building;
        this.requestedAccessLevel = requestedAccessLevel;
    }

    /**
     * Gets the building for which access is requested.
     *
     * @return the building
     */
    public Building getBuilding() {
        return building;
    }

    /**
     * Sets the building for which access is requested.
     *
     * @param building the building to set
     */
    public void setBuilding(Building building) {
        this.building = building;
    }

    /**
     * Gets the requested access level.
     *
     * @return the requested access level
     */
    public String getRequestedAccessLevel() {
        return requestedAccessLevel;
    }

    /**
     * Sets the requested access level.
     *
     * @param requestedAccessLevel the requested access level to set
     */
    public void setRequestedAccessLevel(String requestedAccessLevel) {
        this.requestedAccessLevel = requestedAccessLevel;
    }
}