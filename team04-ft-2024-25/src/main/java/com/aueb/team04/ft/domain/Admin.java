package com.aueb.team04.ft.domain;

import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Entity representing an Admin user.
 */
@Entity
@DiscriminatorValue("ADMIN")
public class Admin extends User {
    /**
     * The set of requests last modified by this admin.
     */
    @JsonbTransient
    @OneToMany(mappedBy = "lastModifiedBy", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Request> modifiedRequests = new HashSet<>();

    /**
     * Default constructor.
     */
    public Admin() {}

    /**
     * Constructor with parameters.
     *
     * @param username the username of the admin
     * @param password the password of the admin
     * @param email the email of the admin
     */
    public Admin(String username, String password, String email) {
        super(username, password, email);
    }

    /**
     * Adds a request to the set of requests last modified by this admin.
     * @param request
     */
    public void addModifiedRequest(Request request) {
        modifiedRequests.add(request);
    }

    /**
     * Gets the set of requests last modified by this admin.
     *
     * @return the set of modified requests
     */
    public Set<Request> getModifiedRequests() {
        return modifiedRequests;
    }

    /**
     * Sets the set of requests last modified by this admin.
     *
     * @param modifiedRequests the set of modified requests to set
     */
    public void setModifiedRequests(Set<Request> modifiedRequests) {
        this.modifiedRequests = modifiedRequests;
    }

    /**
     * Removes a request from the set of requests last modified by this admin.
     * @param request
     */
    public void removeModifiedRequest(Request request) {
        modifiedRequests.remove(request);
    }
}