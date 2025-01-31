package com.aueb.team04.ft.domain;

import jakarta.json.bind.annotation.JsonbDateFormat;
import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entity representing a Request.
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "request")
@DiscriminatorColumn(name = "request_type", discriminatorType = DiscriminatorType.STRING)
public class Request {

    /**
     * The unique identifier for the request.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "request_id")

    private Long requestID;

    /**
     * The user making the request.
     */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    @JsonbTransient
    private User user;

    /**
     * The timestamp of the request.
     */
    @JsonbDateFormat("yyyy-MM-dd HH:mm:ss")
    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    /**
     * The status of the request.
     */
    @Enumerated(EnumType.STRING)
    @Column
    private RequestStatus status = RequestStatus.PENDING; // Default to Pending.

    /**
     * The admin who last modified the request.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "last_modified_by", nullable = true)
    private Admin lastModifiedBy; // Admin who last modified the request.

    /**
     * Default constructor.
     */
    public Request() {
        this.timestamp = LocalDateTime.now();
    }

    /**
     * Constructor with parameters.
     *
     * @param user the user making the request
     */
    public Request(User user) {
        this.user = user;
        this.timestamp = LocalDateTime.now();
    }

    /**
     * Gets the unique identifier for the request.
     *
     * @return the unique identifier
     */

    public Long getRequestID() {
        return requestID;
    }

    /**
     * Gets the user making the request.
     *
     * @return the user making the request
     */
    public User getUser() {
        return user;
    }

    /**
     * Gets the timestamp of the request.
     *
     * @return the timestamp of the request
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * Gets the status of the request.
     *
     * @return the status of the request
     */
    public RequestStatus getStatus() {
        return status;
    }

    /**
     * Gets the admin who last modified the request.
     *
     * @return the admin who last modified the request
     */
    public Admin getLastModifiedBy() {
        return lastModifiedBy;
    }

    /**
     * Approves the request.
     */
    public void approve() {
        this.status = RequestStatus.APPROVED;
    }


    public void disapprove() {
        this.status = RequestStatus.DISAPPROVED;
    }

    /**
     * Sets the admin who last modified the request.
     *
     * @param admin the admin who last modified the request
     */
    public void setLastModifiedBy(Admin admin) {
        if (admin != null) {
            this.lastModifiedBy = admin;
        } else {
            throw new IllegalArgumentException("Admin cannot be null.");
        }
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public boolean isApproved() {
        return (this.getStatus().equals(RequestStatus.APPROVED));
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}