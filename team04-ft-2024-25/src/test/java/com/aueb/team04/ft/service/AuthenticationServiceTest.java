package com.aueb.team04.ft.service;

import com.aueb.team04.ft.domain.Admin;
import com.aueb.team04.ft.domain.Employee;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class AuthenticationServiceTest {
    @Inject
    AuthenticationService authenticationService;

    @Test
    void authenticateAdminSuccess() {
        AuthenticationService.AuthenticatedUser result = authenticationService.authenticate("admin5555", "admin5555");

        assertNotNull(result);
        assertEquals(AuthenticationService.UserType.ADMIN, result.getUserType());
        assertInstanceOf(Admin.class, result.getUser());
        assertEquals("admin5555", ((Admin) result.getUser()).getUsername());
    }

    @Test
    void authenticateAdminWrongPassword() {
        AuthenticationService.AuthenticatedUser result = authenticationService.authenticate("admin5555", "wrongPassword");

        assertNotNull(result);
        assertEquals(AuthenticationService.UserType.NONE, result.getUserType());
        assertNull(result.getUser());
    }

    @Test
    void authenticateEmployeeSuccess() {
        AuthenticationService.AuthenticatedUser result = authenticationService.authenticate("employee1111", "employee1111");

        assertNotNull(result);
        assertEquals(AuthenticationService.UserType.EMPLOYEE, result.getUserType());
        assertInstanceOf(Employee.class, result.getUser());
        assertEquals("employee1111", ((Employee) result.getUser()).getUsername());
    }

    @Test
    void authenticateEmployeeWrongPassword() {
        AuthenticationService.AuthenticatedUser result = authenticationService.authenticate("employee1111", "wrongPassword");

        assertNotNull(result);
        assertEquals(AuthenticationService.UserType.NONE, result.getUserType());
        assertNull(result.getUser());
    }

    @Test
    void authenticateInvalidUser() {
        AuthenticationService.AuthenticatedUser result = authenticationService.authenticate("invalidUser", "invalidPass");

        assertNotNull(result);
        assertEquals(AuthenticationService.UserType.NONE, result.getUserType());
        assertNull(result.getUser());
    }
}
