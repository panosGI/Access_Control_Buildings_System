package com.aueb.team04.ft.security;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class SecurityFilterTest {

    @Test
    void testFilterWithValidAdminHeaders() {
        given()
                .header("X-Username", "admin5555")
                .header("X-Password", "admin5555")
                .when()
                .get("/building")
                .then()
                .statusCode(200)
                .body(is(not(empty()))); // Check if the body is not empty
    }

    @Test
    void testFilterWithValidEmployeeHeaders() {
        given()
                .header("X-Username", "employee1111")
                .header("X-Password", "employee1111")
                .when()
                .get("/building/1111")
                .then()
                .statusCode(200);
    }

    @Test
    void testFilterWithInvalidHeaders() {
        given()
                .header("X-Username", "wrongUser")
                .header("X-Password", "wrongPass")
                .when()
                .get("/building/1111")
                .then()
                .statusCode(401)
                .body(equalTo("Authentication failed"));
    }

    @Test
    void testFilterWithJsonBodyCredentials() {
        String jsonBody = """
                {
                     "employee": {
                         "password": "employee1111",
                         "username": "employee1111"
                     }
                 }
            """;

        given()
                .contentType(ContentType.JSON)
                .body(jsonBody)
                .when()
                .get("/building/1111")
                .then()
                .statusCode(200);
    }

    @Test
    void testFilterWithInvalidJsonBodyCredentials() {
        String jsonBody = """
            {
                "employee": {
                    "username": "employee1111",
                    "password": "wrongPassword"
                }
            }
            """;

        given()
                .contentType(ContentType.JSON)
                .body(jsonBody)
                .when()
                .get("/building/1111")
                .then()
                .statusCode(401);
    }

//    @Test
//    void testFilterSkipsRegistrationEndpoint() {
//        String jsonBody = """
//                {
//                       "username": "newuser",
//                       "password": "newpassword",
//                       "email": "newuser@example.com"
//                     }
//            """;
//        given()
//                .contentType(ContentType.JSON)
//                .body(jsonBody)
//                .when()
//                .post("/registration") // Skips authentication
//                .then()
//                .statusCode(201);
//    }

    @Test
    void testFilterWithMissingCredentialsInHeaders() {
        given()
                .when()
                .get("/building/1111")
                .then()
                .statusCode(500);
    }

    @Test
    void testFilterWithMissingCredentialsInJsonBody() {
        String jsonBody = "{}";  // Empty JSON body with no username or password

        given()
                .contentType(ContentType.JSON)
                .body(jsonBody)
                .when()
                .get("/building")
                .then()
                .statusCode(401)
                .body(is("Missing username or password"));
    }

    @Test
    void testFilterWithNoEmployeeOrAdminInJsonBody() {
        String jsonBody = """
            {
                "user": {
                    "username": "someuser",
                    "password": "somepassword"
                }
            }
            """;

        given()
                .contentType(ContentType.JSON)
                .body(jsonBody)
                .when()
                .get("/building")
                .then()
                .statusCode(401)
                .body(is("Missing username or password"));
    }

    @Test
    void testFilterWithMissingPasswordInJsonBody() {
        String jsonBody = """
            {
                "admin": {
                    "username": "admin5555"
                }
            }
            """;

        given()
                .contentType(ContentType.JSON)
                .body(jsonBody)
                .when()
                .get("/building")
                .then()
                .statusCode(401)
                .body(is("Missing username or password"));
    }

    @Test
    void testFilterWithValidAdminJsonBody() {
        String jsonBody = """
            {
                "admin": {
                    "username": "admin5555",
                    "password": "admin5555"
                }
            }
            """;

        given()
                .contentType(ContentType.JSON)
                .body(jsonBody)
                .when()
                .get("/building")
                .then()
                .statusCode(200);
    }

    @Test
    void testFilterWithMissingHeadersAndBody() {
        String jsonBody = "{}";  // Empty JSON body with no username or password

        given()
                .contentType(ContentType.JSON)
                .body(jsonBody)
                .when()
                .get("/building")
                .then()
                .statusCode(401)
                .body(is("Missing username or password"));
    }

    @Test
    void testSecurityContextSetWithValidCredentials() {
        String jsonBody = """
            {
                "admin": {
                    "username": "admin5555",
                    "password": "admin5555"
                }
            }
            """;

        given()
                .contentType(ContentType.JSON)
                .body(jsonBody)
                .when()
                .get("/building")
                .then()
                .statusCode(200);
    }

    @Test
    void testSecureRequestWithValidCredentials() {
        String jsonBody = """
            {
                "admin": {
                    "username": "admin5555",
                    "password": "admin5555"
                }
            }
            """;

        given()
                .contentType(ContentType.JSON)
                .body(jsonBody)
                .when()
                .get("/building/1111")
                .then()
                .statusCode(200);
    }

    @Test
    void testFilterWithInvalidCredentials() {
        String jsonBody = """
            {
                "admin": {
                    "username": "admin5555",
                    "password": "wrongpassword"
                }
            }
            """;

        given()
                .contentType(ContentType.JSON)
                .body(jsonBody)
                .when()
                .get("/building/1111")
                .then()
                .statusCode(401)
                .body(is("Authentication failed"));
    }
}
