package com.aueb.team04.ft.resource;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import com.aueb.team04.ft.representation.AccessLogRepresentation;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.List;

@QuarkusTest
public class AccessLogResourceTest {

    @Test
    public void testGetAccessLogs_Success() {
        String jsonBody = """
            {
                "admin": {
                    "id": 5555,
                    "username": "admin5555",
                    "email": "admin5555@email",
                    "password": "admin5555"
                }
            }
            """;

        List<AccessLogRepresentation> accessLogs = given()
                .contentType(ContentType.JSON)
                .body(jsonBody)
                .when()
                .get("/access_log")
                .then()
                .statusCode(200)
                .extract().as(new TypeRef<List<AccessLogRepresentation>>() {});

        // Assertions.assertTrue(accessLogs.size() == 3 || accessLogs.size() == 4);
    }

    @Test
    public void testGetAccessLogs_Unauthorized() {
        String jsonBody = """
            {
                "employee": {
                    "password": "employee1111",
                    "username": "employee1111"
                }
            }
            """;

        Response response = given()
                .contentType(ContentType.JSON)
                .body(jsonBody)
                .when()
                .get("/access_log")
                .then()
                .statusCode(403)
                .contentType(ContentType.BINARY)
                .extract()
                .response();

        // Verify the response type and message
        assertEquals(ContentType.BINARY.toString(), response.getContentType());
        assertEquals("Only ADMINS can view the logs", response.getBody().asString());
    }

    @Test
    public void testGetAccessLogById_Success() {
        String jsonBody = """
            {
                "admin": {
                    "id": 5555,
                    "username": "admin5555",
                    "email": "admin5555@email",
                    "password": "admin5555"
                }
            }
            """;

        AccessLogRepresentation accessLog = given()
                .contentType(ContentType.JSON)
                .body(jsonBody)
                .when()
                .get("/access_log/1111") 
                .then()
                .statusCode(200)
                .extract().as(AccessLogRepresentation.class);

        assertEquals(1111, accessLog.id);
    }

    @Test
    public void testGetAccessLogById_NotFound() {
        String jsonBody = """
            {
                "admin": {
                    "id": 5555,
                    "username": "admin5555",
                    "email": "admin5555@email",
                    "password": "admin5555"
                }
            }
            """;

        Response response = given()
                .contentType(ContentType.JSON)
                .body(jsonBody)
                .when()
                .get("/access_log/9999") 
                .then()
                .statusCode(204)
                .extract()
                .response();

        assertEquals("", response.getContentType());
    }

    @Test
    public void testDeleteAccessLogById_Success() {
        String jsonBody = """
            {
                "admin": {
                    "id": 5555,
                    "username": "admin5555",
                    "email": "admin5555@email",
                    "password": "admin5555"
                }
            }
            """;

        given()
                .contentType(ContentType.JSON)
                .body(jsonBody)
                .when()
                .delete("/access_log/4444") 
                .then()
                .statusCode(204);
    }

    @Test
    public void testDeleteAccessLogById_NotFound() {
        String jsonBody = """
            {
                "admin": {
                    "id": 5555,
                    "username": "admin5555",
                    "email": "admin5555@email",
                    "password": "admin5555"
                }
            }
            """;

        Response response = given()
                .contentType(ContentType.JSON)
                .body(jsonBody)
                .when()
                .delete("/access_log/9999")
                .then()
                .statusCode(404)
                .contentType(ContentType.BINARY)
                .extract()
                .response();

        assertEquals(ContentType.BINARY.toString(), response.getContentType());
        assertEquals("AccessLog not found", response.getBody().asString());
    }

}