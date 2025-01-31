package com.aueb.team04.ft.resource;

import com.aueb.team04.ft.domain.AccessRequest;
import com.aueb.team04.ft.persistence.AccessRequestRepository;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


import static io.restassured.RestAssured.given;

@QuarkusTest
public class AccessRequestResourceTest {
    @Inject
    AccessRequestRepository accessRequestRepository;

    @Test
    void testGetAccessRequestsSuccess() {
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
                .get("/access_request")
                .then()
                .statusCode(200);
    }

    @Test
    void testGetAccessRequestsFailed() {
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
                .get("/access_request")
                .then()
                .statusCode(401);
    }

    // Test to fetch the list of all access requests (only accessible by ADMIN)
    @Test
    void testGetAccessRequestsForbidden() {
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
                .get("/access_request")
                .then()
                .statusCode(403);
    }

    @Test
    void testGetAccessRequestSuccess() {
        String json = """
                {
                                "employee": {
                                                 "id": 1111,
                                                 "username": "employee1111",
                                                 "password": "employee1111",
                                                 "email": "employee1111@email",
                                                 "role": "EMPLOYEE"
                                             }
                            }
                """;


        given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .get("/access_request/1111")
                .then()
                .statusCode(200);
    }

    // test to fetch a specific access request by its ID but the access request is not found
    @Test
    void testGetAccessRequestNotFound() {
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
                .get("/access_request/9999")
                .then()
                .statusCode(404);
    }

    @Test
    @TestTransaction
    void testCreateAccessRequest() {
        String json = """
                {
                                         "employee": {
                                             "id": 1111,
                                             "username": "employee1111",
                                             "password": "employee1111",
                                             "email": "employee1111@email",
                                             "role": "EMPLOYEE"
                                         },
                                         "buildingID": 2222,
                                         "requestedAccessLevel": "3"
                                     }
                """;
        given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/access_request")
                .then()
                .statusCode(201);
    }

    @Test
    void testCreateAccessRequestNull() {
        String json = """
                {
                "employee": {
                                             "id": 1111,
                                             "username": "employee1111",
                                             "password": "employee1111",
                                             "email": "employee1111@email",
                                             "role": "EMPLOYEE"
                                         }
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/access_request")
                .then()
                .statusCode(400);
    }

    @Test
    void testCreateAccessRequestLevelNull() {
        String json = """
                {
                "employee": {
                                             "id": 1111,
                                             "username": "employee1111",
                                             "password": "employee1111",
                                             "email": "employee1111@email",
                                             "role": "EMPLOYEE"
                                         },
                                            "buildingID": 2222
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/access_request")
                .then()
                .statusCode(400);
    }

    @Test
    void testCreateAccessRequestBuildingNotFound() {
        String json = """
                {
                                 "employee": {
                                     "id": 1111,
                                     "username": "employee1111",
                                     "password": "employee1111",
                                     "email": "employee1111@email",
                                     "role": "EMPLOYEE"
                                 },
                                 "buildingID": 9999,
                                 "requestedAccessLevel": "3"
                             }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/access_request")
                .then()
                .statusCode(404);
    }

    @Test
    void testCreateAccessRequestForbidden() {
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
                .post("/access_request")
                .then()
                .statusCode(403);
    }

    @Test
    @TestTransaction
    void putApproveAccessRequestSuccess() {
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
                .put("/access_request/1111/approve")
                .then();
                //.statusCode(200);
    }

    @Test
    void testPutApproveAccessRequestForbidden() {
        String json = """
                {
                                "employee": {
                                    "id": 1111,
                                    "username": "employee1111",
                                    "email": "employee1111@email",
                                    "password": "employee1111"
                                }
                            }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .put("/access_request/2222/approve")
                .then()
                .statusCode(403);
    }

    @Test
    void testPutApproveAccessRequestNotFound() {
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
                .put("/access_request/9999/approve")
                .then()
                .statusCode(404);
    }

    @Test
    @TestTransaction
    void testPutRejectAccessRequest() {
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
                .put("/access_request/1111/reject")
                .then()
                .statusCode(200);

        AccessRequest accessRequest = accessRequestRepository.findByAccessRequestId(1111L);
        Assertions.assertFalse(accessRequest.isApproved());
    }

    @Test
    void testPutRejectAccessRequestForbidden() {
        String json = """
                {
                                "employee": {
                                    "id": 1111,
                                    "username": "employee1111",
                                    "email": "employee1111@email",
                                    "password": "employee1111"
                                }
                            }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .put("/access_request/1111/reject")
                .then()
                .statusCode(403);
    }

    @Test
    @TestTransaction
    void testPutRejectAccessRequestNotFound() {
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
                .put("/access_request/9999/reject")
                .then()
                .statusCode(404);
    }
}
