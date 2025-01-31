package com.aueb.team04.ft.resource;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import com.aueb.team04.ft.representation.AlertRepresentation;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.List;
import org.junit.jupiter.api.Assertions;

@QuarkusTest
public class AlertResourceTest {

    @Test
    public void getAlert_Authorized() {
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

        List<AlertRepresentation> alerts = given()
                .contentType(ContentType.JSON)
                .body(jsonBody).when()
                .get(AccessControlURI.ALERT)
                .then()
                .statusCode(200)
                .extract().as(new TypeRef<List<AlertRepresentation>>() {});

        Assertions.assertTrue(alerts.size() == 4 ||alerts.size() == 5 || alerts.size() == 6);
    }

    @Test
    public void getAlerts_Unauthorized() {
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
                .get("/alert")
                .then()
                .statusCode(403)
                .contentType(ContentType.BINARY)
                .extract()
                .response();

        // Verify the response type and message
        assertEquals(ContentType.BINARY.toString(), response.getContentType());
        assertEquals("Only ADMINS can view the Alerts", response.getBody().asString());
    }

    @Test
    public void testDeleteAlertById_Success() {
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
                .delete("/alert/3333")
                .then()
                .statusCode(204);
    }

    @Test
    public void testDeleteAlertById_NotFound() {
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
                .delete("/alert/9999")
                .then()
                .statusCode(404)
                .contentType(ContentType.BINARY)
                .extract()
                .response();

        // Verify the response type and message
        assertEquals(ContentType.BINARY.toString(), response.getContentType());
        assertEquals("Alert not found", response.getBody().asString());
    }

    @Test
    public void testGetAlertBySeverity_Success() {
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

        List<AlertRepresentation> alerts = given()
                .contentType(ContentType.JSON)
                .body(jsonBody)
                .when()
                .get("/alert/severity/1")
                .then()
                .statusCode(200)
                .extract().as(new TypeRef<List<AlertRepresentation>>() {});

        assertEquals(4, alerts.size());
    }

    // @Test
    // public void testGetAlertBySeverity_Unauthorized() {
    //     String jsonBody = """
    //         {
    //             "employee": {
    //                 "password": "employee1111",
    //                 "username": "employee1111"
    //             }
    //         }
    //         """;

    //     Response response = given()
    //             .contentType(ContentType.JSON)
    //             .body(jsonBody)
    //             .when()
    //             .get("/alert/severity/1")
    //             .then()
    //             .statusCode(403)
    //             .contentType(ContentType.JSON)
    //             .extract()
    //             .response();

    //     // Verify the response type and message
    //     assertEquals(ContentType.JSON.toString(), response.getContentType());
    //     assertEquals("Only ADMINS can view the Alerts", response.getBody().asString());
    // }

}