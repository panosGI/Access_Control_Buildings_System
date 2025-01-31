package com.aueb.team04.ft.domain;

import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Abstract entity representing a User.
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "users")
@DiscriminatorColumn(name = "role", discriminatorType = DiscriminatorType.STRING)
public class User {

    /**
     * The unique identifier for the user.
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    /**
     * The username of the user.
     */
    @Column(name = "username", unique = true)
    private String username;

    /**
     * The password of the user.
     */
    @Column(name = "password")
    private String password;

    /**
     * The email of the user.
     */
    @Column(name = "email")
    private String email;

    /**
     * The set of requests made by this user.
     */
    @JsonbTransient
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Request> requests = new HashSet<>();

    /**
     * Default constructor.
     */
    public User() {}

    /**
     * Constructor with parameters.
     *
     * @param username the username of the user
     * @param password the password of the user
     * @param email the email of the user
     */
    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    /**
     * Gets the unique identifier for the user.
     *
     * @return the unique identifier
     */
    public Long getId() {
        return id;
    }

    /**
     * Gets the username of the user.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of the user.
     *
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the password of the user.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password of the user.
     *
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the email of the user.
     *
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email of the user.
     *
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the set of requests made by this user.
     *
     * @return the set of requests
     */
    public Set<Request> getRequests() {
        return requests;
    }

    /**
     * Sets the set of requests made by this user.
     *
     * @param requests the set of requests to set
     */
    public void setRequests(Set<Request> requests) {
        this.requests = requests;
    }

    /**
     * Adds a request to the set of requests made by this user.
     *
     * @param request the request to add
     */
    public void addRequest(Request request) {
        requests.add(request);
    }
}