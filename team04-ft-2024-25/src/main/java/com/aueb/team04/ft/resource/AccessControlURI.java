package com.aueb.team04.ft.resource;

public class AccessControlURI {
    // Creates an employee account
    // Always gets a 201 Created response
    public static final String REGISTRATION = "/registration";

    // Admin can view all employees
    // Admin can view a specific employee
    public static final String EMPLOYEE = "/employee";

    // POST: Employee submits an access request in order to get an access card
    // GET ALL & UPDATE: Admin can approve or reject the request
    public static final String ACCESS_REQUESTS = "/access_request";

    // Admin can CRUD all access cards
    public static final String ACCESS_CARD = "/access_card";

    // Admin can CRUD Building
    public static final String BUILDING = "/building";

    // Admin can CRUD Access Point
    // checkAccess is a GET method that checks if a user has access to a specific access point
    // hasAccessToPrerequisites is a GET method that checks if a user has access to all prerequisites of a specific access point
    public static final String ACCESS_POINT = "/access_point";

    // Admin can view all access logs
    public static final String ACCESS_LOGS = "/access_log";

    // Admin can view all alerts
    public static final String ALERT = "/alert";
}
