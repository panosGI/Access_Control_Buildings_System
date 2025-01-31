package com.aueb.team04.ft;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class AccessControlExceptionTest {

    @Test
    void testDefaultConstructor() {
        AccessControlException exception = new AccessControlException();
        assertNull(exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testConstructorWithMessage() {
        String message = "Access denied";
        AccessControlException exception = new AccessControlException(message);
        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testConstructorWithMessageAndCause() {
        String message = "Access denied";
        Throwable cause = new Throwable("Cause of the exception");
        AccessControlException exception = new AccessControlException(message, cause);
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void testConstructorWithCause() {
        Throwable cause = new Throwable("Cause of the exception");
        AccessControlException exception = new AccessControlException(cause);
        assertEquals(cause, exception.getCause());
        assertEquals("java.lang.Throwable: Cause of the exception", exception.getMessage());
    }
}