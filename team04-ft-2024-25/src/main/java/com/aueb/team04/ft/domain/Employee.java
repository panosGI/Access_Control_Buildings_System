package com.aueb.team04.ft.domain;

import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Entity representing an Employee user.
 */
@Entity
@DiscriminatorValue("EMPLOYEE")
public class Employee extends User {

    /**
     * The set of requests made by this employee.
     */
    @JsonbTransient
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<Request> requests = new HashSet<>();

    /**
     * The access card associated with this employee.
     */
    @JsonbTransient
    @OneToOne(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    private AccessCard accessCard;

    /**
     * Default constructor.
     */
    public Employee() {
    }

    /**
     * Constructor with parameters.
     *
     * @param username the username of the employee
     * @param password the password of the employee
     * @param email    the email of the employee
     */
    public Employee(String username, String password, String email) {
        super(username, password, email);
    }

    /**
     * Gets the access card associated with this employee.
     *
     * @return the access card
     */
    public AccessCard getAccessCard() {
        return accessCard;
    }

    /**
     * Sets the access card associated with this employee.
     *
     * @param accessCard the access card to set
     */
    public void setAccessCard(AccessCard accessCard) {
        this.accessCard = accessCard;
    }

    /**
     * Gets the set of requests made by this employee.
     *
     * @return the set of requests
     */
    public Set<Request> getRequests() {
        return requests;
    }

    /**
     * Adds a request to the set of requests made by this employee.
     *
     * @param request the request to add
     */
    public void addRequest(Request request) {
        this.requests.add(request);
    }
}