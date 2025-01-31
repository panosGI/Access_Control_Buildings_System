package com.aueb.team04.ft.resource;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

@QuarkusTest
public class AccessPointResourceTest {

//    @Test
//    public void testGetAccessPoints_Success() {
//        String jsonBody = """
//            {
//                "admin": {
//                    "id": 5555,
//                    "username": "admin5555",
//                    "email": "admin5555@email",
//                    "password": "admin5555"
//                }
//            }
//            """;
//
//        Response response = given()
//                .contentType(ContentType.JSON)
//                .body(jsonBody)
//                .when()
//                .get("/building/1111/access_point")
//                .then()
//                .statusCode(200)
//                .contentType(ContentType.JSON)
//                .extract()
//                .response();
//
//        assertEquals(ContentType.JSON.toString(), response.getContentType());
//    }

    // @Test
    // public void testGetAccessPoints_BadRequest() {
    //     String jsonBody = """
    //         {
    //             "admin": {
    //                 "id": 5555,
    //                 "username": "admin5555",
    //                 "email": "admin5555@email",
    //                 "password": "admin5555"
    //             }
    //         }
    //         """;

    //     Response response = given()
    //             .contentType(ContentType.JSON)
    //             .body(jsonBody)
    //             .when()
    //             .get("/buildings/9999/access_point")
    //             .then()
    //             .statusCode(404)
    //             .extract()
    //             .response();

    //     // Verify the response type and message
    //     assertEquals(ContentType.JSON.toString(), response.getContentType());
    //     assertEquals("Building ID is required", response.getBody().asString());
    // }

    @Test
    public void testAccess_Success_Admin() {
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
                .get("/building/1111/access_point/1111") // Use an ID that exists
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract()
                .response();

        assertEquals(ContentType.JSON.toString(), response.getContentType());
        // Add more assertions to verify the content of the access point if needed
    }

    @Test
    public void testAccess_Success_Employee_AccessGranted() {
        String jsonBody = """
            {
                "employee": {
                    "id": 1111,
                    "username": "employee1111",
                    "email": "employee1111@email",
                    "password": "employee1111"
                }
            }
            """;

        Response response = given()
                .contentType(ContentType.JSON)
                .body(jsonBody)
                .when()
                .get("/building/1111/access_point/1111") // Use an ID that exists and the employee has access to
                .then()
                .statusCode(200)
                .extract()
                .response();

        assertEquals("Access Granted!", response.getBody().asString());
    }

    @Test
    public void testAccess_Employee_NotAllowedToPass() {
        String jsonBody = """
            {
                "employee": {
                    "id": 1111,
                    "username": "employee1111",
                    "email": "employee1111@email",
                    "password": "employee1111"
                }
            }
            """;

        Response response = given()
                .contentType(ContentType.JSON)
                .body(jsonBody)
                .when()
                .get("/building/1111/access_point/3333") // Use an ID that exists but the employee does not have access to
                .then()
                .statusCode(403)
                .extract()
                .response();

        assertEquals("You are not allowed to pass this access point", response.getBody().asString());
    }

    @Test
    public void testAccess_Employee_NotAllowedToBeThere() {
        String jsonBody = """
            {
                "employee": {
                    "id": 1111,
                    "username": "employee1111",
                    "email": "employee1111@email",
                    "password": "employee1111"
                }
            }
            """;

        Response response = given()
                .contentType(ContentType.JSON)
                .body(jsonBody)
                .when()
                .get("/building/1111/access_point/5555") // Use an ID that exists but the employee is not allowed to be there
                .then()
                .statusCode(403)
                .extract()
                .response();

        assertEquals("You are not allowed to be in this access point", response.getBody().asString());
    }

    // @Test
    // public void testAccess_BadRequest() {
    //     String jsonBody = """
    //         {
    //             "admin": {
    //                 "id": 5555,
    //                 "username": "admin5555",
    //                 "email": "admin5555@email",
    //                 "password": "admin5555"
    //             }
    //         }
    //         """;

    //     Response response = given()
    //             .contentType(ContentType.JSON)
    //             .body(jsonBody)
    //             .when()
    //             .get("/building/null/access_point/1") // Use a null building ID to trigger bad request
    //             .then()
    //             .statusCode(400)
    //             .extract()
    //             .response();

    //     assertEquals("Building ID is required", response.getBody().asString());
    // }

    @Test
    public void testAccess_NotFound() {
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
                .get("/building/1111/access_point/9999") // Use an ID that does not exist
                .then()
                .statusCode(404)
                .extract()
                .response();

        assertEquals("Access Point not found", response.getBody().asString());
    }

    @Test
    public void testCreateAccessPoint_Success() {
        String jsonBody = """
            {
                "admin": {
                    "id": 5555,
                    "username": "admin5555",
                    "email": "admin5555@email",
                    "password": "admin5555"
                },

            "accessPoint":{
            "location": "Test IN_BETWEEN",
            "type": "IN_BETWEEN",
            "accessLevelRequired": "1",
            "accessLogs": [],
            "prerequisites": []
                }
            }
            """;

        Response response = given()
                .contentType(ContentType.JSON)
                .body(jsonBody)
                .when()
                .post("building/1111/access_point")
                .then()
                .statusCode(201)
                .extract()
                .response();

        // Verify the response type and content
        assertEquals("", response.getContentType());
    }
    
    @Test
    public void testGetAccessLogsByAccessPoint_Success() {
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
                .get("/building/1111/access_point/1111/access_log") 
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract()
                .response();

        assertEquals(ContentType.JSON.toString(), response.getContentType());
    }

    @Test
    public void testGetAccessLogsByAccessPoint_NotFound() {
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
                .get("building/1111/access_point/9999/access_log") // Use an ID that does not exist
                .then()
                .statusCode(200)
                .extract()
                .response();

        assertEquals("[]", response.getBody().asString());
    }

    @Test
    public void testAddPrerequisite_Success() {
        // Ensure that the access points are properly set up
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
                .post("/access_point/5555/3333")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract()
                .response();

        assertEquals(ContentType.JSON.toString(), response.getContentType());
    }

    @Test
    public void testAddPrerequisite_NotFound() {
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
                .post("/access_point/5555/9999") // Use a source ID that does not exist
                .then()
                .statusCode(404)
                .extract()
                .response();

        assertEquals("Access Point not found", response.getBody().asString());
    }

    @Test
    public void testAddPrerequisite_TargetNotFound() {
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
                .post("/access_points/9999/3333") // Use a target ID that does not exist
                .then()
                .statusCode(404)
                .extract()
                .response();
    }

    @Test
    public void testGetPrerequisites_Success() {
        
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
                .get("/access_point/5555/prerequisites") // Use an ID that exists
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract()
                .response();

        assertEquals(ContentType.JSON.toString(), response.getContentType());
    }

    @Test
    public void testGetPrerequisites_NotFound() {
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
                .get("/access_point/9999/prerequisites") // Use an ID that does not exist
                .then()
                .statusCode(404)
                .extract()
                .response();

        assertEquals("Access Point not found", response.getBody().asString());
    }
}
