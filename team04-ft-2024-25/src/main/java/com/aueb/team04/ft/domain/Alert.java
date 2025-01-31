package com.aueb.team04.ft.domain;

import jakarta.persistence.*;

/**
 * Entity representing an Alert.
 */
@Entity
@Table(name = "alert")
public class Alert {

    /**
     * The unique identifier for the alert.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * The type of the alert.
     */
    @Column(name = "alertType")
    private String alertType;

    /**
     * The severity level of the alert.
     */
    @Column(name = "severity")
    private int severity;

    /**
     * The access log associated with the alert.
     */
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "accessLog_id")
    private AccessLog accessLog;

    /**
     * Default constructor.
     */
    public Alert() {}

    /**
     * Constructor with parameters.
     *
     * @param alertType the type of the alert
     * @param severity the severity level of the alert
     */
    public Alert(String alertType, int severity) {
        this.alertType = alertType;
        this.severity = severity;
    }

    /**
     * Gets the unique identifier for the alert.
     *
     * @return the unique identifier
     */
    public Long getId() {
        return id;
    }

    /**
     * Gets the type of the alert.
     *
     * @return the type of the alert
     */

    public String getAlertType() {
        return alertType;
    }

    /**
     * Sets the type of the alert.
     *
     * @param alertType the type of the alert to set
     */
    public void setAlertType(String alertType) {
        this.alertType = alertType;
    }

    /**
     * Gets the severity level of the alert.
     *
     * @return the severity level of the alert
     */
    public int getSeverity() {
        return severity;
    }

    /**
     * Sets the severity level of the alert.
     *
     * @param severity the severity level of the alert to set
     */
    public void setSeverity(int severity) {
        this.severity = severity;
    }

    /**
     * Links the alert to an access log.
     *
     * @param accessLog the access log to link
     */
    public void setAccessLog(AccessLog accessLog) {
        this.accessLog = accessLog;
    }

    /**
     * Gets the access log associated with the alert.
     *
     * @return the access log
     */
    public AccessLog getAccessLog() {
        return this.accessLog;
    }

    /**
     * Returns a string representation of the alert.
     *
     * @return a string representation of the alert
     */
    @Override
    public String toString() {
        return "Security Alert of severity level: " + this.severity + " of type: " + this.alertType;
    }

}