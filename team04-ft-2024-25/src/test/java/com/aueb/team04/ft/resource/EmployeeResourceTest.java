package com.aueb.team04.ft.resource;


import com.aueb.team04.ft.representation.EmployeeRepresentation;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;


@QuarkusTest
public class EmployeeResourceTest {

    // Test to fetch the list of all employees (only accessible by ADMIN)
    @Test
    void testGetEmployeesSuccess() {
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

        List<EmployeeRepresentation> employees = given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .get("/employee")
                .then()
                .statusCode(200)
                .extract().as(new TypeRef<>() {
                });
    }

    @Test
    void testGetEmployeesAuthenticationFailed() {
        String json = """
                {
                                "admin": {
                                    "id": 5555,
                                    "username": "admin5555",
                                    "email": "admin5555@email",
                                    "password": "wrongPassword"
                                }
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .get("/employee")
                .then()
                .statusCode(401);

    }

    @Test
    void testGetEmployeesForbidden() {
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
                .get("/employee")
                .then()
                .statusCode(403);
    }

    // Test to fetch an individual employee by ID (only accessible by ADMIN)
    @Test
    void testGetEmployeeSuccess() {
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

        EmployeeRepresentation employee = given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .get("/employee/1111")
                .then()
                .statusCode(200)
                .extract().as(EmployeeRepresentation.class);

        Assertions.assertEquals(1111, employee.id);
    }

    @Test
    void testGetEmployeeForbidden() {
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
                .get("/employee/1111")
                .then()
                .statusCode(403);
    }

    @Test
    void testGetEmployeeNotFound() {
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
                .get("/employee/9999")
                .then()
                .statusCode(404);

    }

    // Test to delete an employee by ID (only accessible by ADMIN)
    @TestTransaction
    @Test
    void testDeleteEmployeeSuccess() {
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
                .delete("/employee/3333")
                .then()
                .statusCode(200);
    }

    @Test
    void testDeleteEmployeeNotFound() {
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
                .delete("/employee/9999")
                .then()
                .statusCode(404);
    }

    @Test
    void testDeleteEmployeeForbidden() {
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
                .delete("/employee/1111")
                .then()
                .statusCode(403);
    }
}

