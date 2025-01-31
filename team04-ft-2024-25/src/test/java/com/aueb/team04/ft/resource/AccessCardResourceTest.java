package com.aueb.team04.ft.resource;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class AccessCardResourceTest {
    @Test
    void testGetAccessCardsSuccess() {
        String json = """
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
                .body(json)
                .when()
                .get("/access_card")
                .then()
                .statusCode(200);
    }

    @Test
    void testGetAccessCardsForbidden() {
        String json = """
                {
                
                                 "employee": {
                                     "password": "employee1111",
                                     "username": "employee1111"
                                 }
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .get("/access_card")
                .then()
                .statusCode(403);
    }

    @Test
    void testDelegationToAccessLog() {
        String json = """
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
                .body(json)
                .when()
                .get("/access_card/1111/access_log")
                .then()
                .statusCode(200);
    }
}
