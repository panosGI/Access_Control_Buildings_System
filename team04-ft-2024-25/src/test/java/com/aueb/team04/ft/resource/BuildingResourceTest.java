package com.aueb.team04.ft.resource;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import com.aueb.team04.ft.representation.BuildingRepresentation;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

@QuarkusTest
public class BuildingResourceTest {

    @Test
    public void testGetBuildings_Success() {
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

        List<BuildingRepresentation> buildings = given()
                .contentType(ContentType.JSON)
                .body(jsonBody)
                .when()
                .get("/building")
                .then()
                .statusCode(200)
                .extract().as(new TypeRef<List<BuildingRepresentation>>() {});

        // assertEquals(3, buildings.size());
    }

    @Test
    public void testGetBuildings_Unauthorized() {
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
                .get("/building")
                .then()
                .statusCode(403)
                .contentType(ContentType.JSON)
                .extract()
                .response();

        // Verify the response type and message
        assertEquals(ContentType.JSON.toString(), response.getContentType());
        assertEquals("Only ADMINS can view all buildings", response.getBody().asString());
    }

    @Test
    public void testGetBuilding_Admin_Success() {
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
                .get("/building/1111") 
                .then()
                .statusCode(200)
                .extract()
                .response();

        assertEquals(ContentType.JSON.toString(), response.getContentType());
    }

    @Test
    public void testGetBuilding_Employee_Success() {
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
                .get("/building/1111")
                .then()
                .statusCode(200)
                .extract()
                .response();

        assertEquals(ContentType.JSON.toString(), response.getContentType());
    }

    @Test
    public void testGetBuilding_Employee_Forbidden() {
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
                .get("/building/2222") // Use an ID that exists but the employee does not have access to
                .then()
                .statusCode(403)
                .extract()
                .response();

        assertEquals(ContentType.JSON.toString(), response.getContentType());
        assertEquals("You do not have access to this building", response.getBody().asString());
    }

    @Test
    public void testGetBuilding_NotFound() {
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
                .get("/building/9999") // Use an ID that does not exist
                .then()
                .statusCode(404)
                .extract()
                .response();

        assertEquals(ContentType.JSON.toString(), response.getContentType());
        assertEquals("Building not found", response.getBody().asString());
    }

    // @Test
    // public void testGetAccessPoints_NotFound() {
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
    //             .get("/building/9999/access_point") // Use an ID that does not exist
    //             .then()
    //             .statusCode(404)
    //             .contentType(ContentType.BINARY)
    //             .extract()
    //             .response();

    //     assertEquals(ContentType.BINARY.toString(), response.getContentType());
    //     assertEquals("Building not found", response.getBody().asString());
    // }

    // @Test
    // public void testGetAccessPoints_Success() {
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
    //             .get("/building/1111/access_point") // Use an ID that exists
    //             .then()
    //             .statusCode(200)
    //             .contentType(ContentType.JSON)
    //             .extract()
    //             .response();

    //     assertEquals(ContentType.JSON.toString(), response.getContentType());
    // }

    @Test
    public void testCreateBuilding_Success() {
        String jsonBody = """
            {
                "admin": {
                    "id": 5555,
                    "username": "admin5555",
                    "email": "admin5555@email",
                    "password": "admin5555"
                },
                "building": {
                    "name": "Building 1",
                    "belongsTo": "AUEB",
                    "address": {
                        "streetName": "Main Street",
                        "streetNumber": "123",
                        "zipcode": "12345"
                    }
                }
            }
            """; 

        Response response = given()
                .contentType(ContentType.JSON)
                .body(jsonBody)
                .when()
                .post("/building")
                .then()
                .statusCode(201)
                .extract()
                .response();

        // Verify the response type and content
        assertEquals(ContentType.JSON.toString(), response.getContentType());
    }

    @Test
    public void testCreateBuilding_Unauthorized() {
        String jsonBody = """
            {
                "employee": {
                    "password": "employee1111",
                    "username": "employee1111"
                },
                "building": {
                    "name": "Building 1",
                    "belongsTo": "AUEB",
                    "address": {
                        "streetName": "Main Street",
                        "streetNumber": "123",
                        "zipcode": "12345"
                    }
                }
            }
            """; 

        Response response = given()
                .contentType(ContentType.JSON)
                .body(jsonBody)
                .when()
                .post("/building")
                .then()
                .statusCode(403)
                .extract()
                .response();

        // Verify the response type and message
        assertEquals(ContentType.JSON.toString(), response.getContentType());
        assertEquals("Only ADMINS can create buildings", response.getBody().asString());
    }

    @Test
    public void testCreateBuilding_BadRequest() {
        String jsonBody = """
            {
                "admin": {
                    "id": 5555,
                    "username": "admin5555",
                    "email": "admin5555@email",
                    "password": "admin5555"
                },
                "building": null
            }
            """;

        Response response = given()
                .contentType(ContentType.JSON)
                .body(jsonBody)
                .when()
                .post("/building")
                .then()
                .statusCode(400)
                .extract()
                .response();

        // Verify the response type and message
        assertEquals(ContentType.JSON.toString(), response.getContentType());
        assertEquals("Invalid request payload", response.getBody().asString());
    }

    @Test
    public void testUpdateBuilding_Success() {
        String jsonBody = """
            {
                "admin": {
                    "id": 5555,
                    "username": "admin5555",
                    "email": "admin5555@email",
                    "password": "admin5555"
                },
                    "name": "Building 1-NEW",
                    "belongsTo": "AUEB-NEW",
                    "address": {
                        "streetName": "Main Street-NEW",
                        "streetNumber": "123-NEW",
                        "zipcode": "12345-NEW"
                    }
            }
            """;

        Response response = given()
                .contentType(ContentType.JSON)
                .body(jsonBody)
                .when()
                .put("/building/2222")
                .then()
                .statusCode(201)
                .extract()
                .response();

        // Verify the response type and content
        assertEquals(ContentType.JSON.toString(), response.getContentType());
    }

    @Test
    public void testUpdateBuilding_Unauthorized() {
        String jsonBody = """
            {
                "employee": {
                    "password": "employee1111",
                    "username": "employee1111"
                },
                    "name": "Building 1-NEW",
                    "belongsTo": "AUEB-NEW",
                    "address": {
                        "streetName": "Main Street-NEW",
                        "streetNumber": "123-NEW",
                        "zipcode": "12345-NEW"
                    }
            }
            """;

        Response response = given()
                .contentType(ContentType.JSON)
                .body(jsonBody)
                .when()
                .put("/building/2222")
                .then()
                .statusCode(403)
                .extract()
                .response();

        // Verify the response type and message
        assertEquals(ContentType.JSON.toString(), response.getContentType());
        assertEquals("Only ADMINS can update buildings", response.getBody().asString());
    }

    @Test
    public void testUpdateBuilding_BadRequest_NullBuilding() {
        String jsonBody = """
            {
                "admin": {
                    "id": 5555,
                    "username": "admin5555",
                    "email": "admin5555@email",
                    "password": "admin5555"
                },
                "building": null
            }
            """;

        Response response = given()
                .contentType(ContentType.JSON)
                .body(jsonBody)
                .when()
                .put("/building/2222")
                .then()
                .statusCode(400)
                .extract()
                .response();

        // Verify the response type and message
        assertEquals(ContentType.JSON.toString(), response.getContentType());
        assertEquals("Invalid request payload", response.getBody().asString());
    }

    @Test
    public void testUpdateBuilding_BadRequest_MissingName() {
        String jsonBody = """
            {
                "admin": {
                    "id": 5555,
                    "username": "admin5555",
                    "email": "admin5555@email",
                    "password": "admin5555"
                },
                "building": {
                    "name": null,
                    "address": "456 Main St",
                    "belongsTo": "New Owner"
                }
            }
            """;

        Response response = given()
                .contentType(ContentType.JSON)
                .body(jsonBody)
                .when()
                .put("/building/2222")
                .then()
                .statusCode(400)
                .extract()
                .response();

        // Verify the response type and message
        assertEquals(ContentType.JSON.toString(), response.getContentType());
        assertEquals("Invalid request payload", response.getBody().asString());
    }

    @Test
    public void testUpdateBuilding_NotFound() {
        String jsonBody = """
            {
                "admin": {
                    "id": 5555,
                    "username": "admin5555",
                    "email": "admin5555@email",
                    "password": "admin5555"
                },
                "building": {
                    "name": "Updated Building",
                    "address": "456 Main St",
                    "belongsTo": "New Owner"
                }
            }
            """;

        Response response = given()
                .contentType(ContentType.JSON)
                .body(jsonBody)
                .when()
                .put("/building/3425") 
                .then()
                .statusCode(400)
                .contentType(ContentType.JSON)
                .extract()
                .response();

        // Verify the response type and message
        assertEquals(ContentType.JSON.toString(), response.getContentType());
        // assertEquals("Building not found", response.getBody().asString());
    }

}