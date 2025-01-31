package com.aueb.team04.ft.resource;


import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import static org.hamcrest.Matchers.*;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class RegistrationResourceTest {
    @Transactional
    @Test
    void testSubmitEmployeeInfoSuccess() {
            String requestBody = """
            {
                "username": "newuser",
                "password": "newpassword",
                "email": "newuser@example.com"
            }
            """;

            given()
                    .contentType(ContentType.JSON)
                    .body(requestBody)
                    .when()
                    .post("/registration")
                    .then()
                    .statusCode(201)
                    .body("username", equalTo("newuser"))
                    .body("password", equalTo("newpassword"))
                    .body("email", equalTo("newuser@example.com"));
        }

    @Test
    void testSubmitEmployeeInfoMissingFields() {
        String requestBody = """
            {
                "username": "newuser"
            }
            """;

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/registration")
                .then()
                .statusCode(400)
                .body(equalTo("Username, password, and email are required"));
    }

    @Test
    void testSubmitEmployeeInfoDuplicateUsername() {
        String requestBody = """
            {
                "username": "employee1111",
                "password": "password123",
                "email": "newuser@example.com"
            }
            """;

        // Simulate existing user in the database
        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/registration");

        // Try registering with the same username
        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/registration")
                .then()
                .statusCode(400)
                .body(equalTo("Username is already in use"));
    }

    @Test
    void testSubmitEmployeeInfoDuplicateEmail() {
        String requestBody1 = """
            {
                "username": "user1",
                "password": "password123",
                "email": "employee1111@email"
            }
            """;
        // Try registering with the same email
        given()
                .contentType(ContentType.JSON)
                .body(requestBody1)
                .when()
                .post("/registration")
                .then()
                .statusCode(400)
                .body(equalTo("Email is already in use"));
    }

    @Test
    void testSubmitEmployeeInfoInvalidContentType() {
        String requestBody = """
            {
                "username": "newuser",
                "password": "newpassword",
                "email": "newuser@example.com"
            }
            """;

        given()
                .contentType(ContentType.XML)
                .body(requestBody)
                .when()
                .post("/registration")
                .then()
                .statusCode(415); // Unsupported Media Type
    }

    @Test
    void testSubmitEmployeeInfoMissingAllFields() {
        String requestBody = "{}";

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/registration")
                .then()
                .statusCode(400)
                .body(equalTo("Username, password, and email are required"));
    }

    @Test
    void testSubmitEmployeeInfoMissingUsername() {
        String requestBody = """
            {
                "password": "password123",
                "email": "user@example.com"
            }
            """;

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/registration")
                .then()
                .statusCode(400)
                .body(equalTo("Username, password, and email are required"));
    }

    @Test
    void testSubmitEmployeeInfoMissingPassword() {
        String requestBody = """
            {
                "username": "user123",
                "email": "user@example.com"
            }
            """;

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/registration")
                .then()
                .statusCode(400)
                .body(equalTo("Username, password, and email are required"));
    }

    @Test
    void testSubmitEmployeeInfoMissingEmail() {
        String requestBody = """
            {
                "username": "user123",
                "password": "password123"
            }
            """;

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/registration")
                .then()
                .statusCode(400)
                .body(equalTo("Username, password, and email are required"));
    }
}
