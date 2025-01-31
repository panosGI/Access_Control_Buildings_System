package com.aueb.team04.ft.domain;

/**
 * Enum representing the status of a request.
 */
public enum RequestStatus {
    /**
     * The request is pending and has not been processed yet.
     */
    PENDING,

    /**
     * The request has been approved.
     */
    APPROVED,

    /**
     * The request has been disapproved.
     */
    DISAPPROVED
}