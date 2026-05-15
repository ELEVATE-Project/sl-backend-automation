package org.shikshalokam.backend.userservice;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.shikshalokam.backend.userServiceUtility.CommonUtilityUserService;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.net.URI;
import java.net.URISyntaxException;

import static io.restassured.RestAssured.given;
import static org.shikshalokam.backend.PropertyLoader.PROP_LIST;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class TestGetListOfUsersByID extends UserServiceBaseTest {

    private static final Logger logger =
            LogManager.getLogger(TestGetListOfUsersByID.class);

    @BeforeMethod
    public void init() {

        logger.info("Logging into User Service");

        CommonUtilityUserService.loginToUser(
                PROP_LIST.get("userservice.qa.phone.login.identifier").toString(),
                PROP_LIST.get("userservice.qa.phone.login.password").toString()
        );
    }

    @Test
    public void testGetListOfUsersById()throws URISyntaxException {

        logger.info("Started calling Search User API");

        URI endpoint = new URI(
                PROP_LIST.get("userservice.search.user.endpoint").toString()
        );

        String requestBody = "{\n" + "  \"user_ids\": [" + PROP_LIST.get("userservice.search.user.id").toString() + "]\n" + "}";

        Response response = given()
                .header(
                        "X-auth-token",
                        CommonUtilityUserService.UserToken
                )
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(endpoint);

        assertEquals(
                response.getStatusCode(),
                200,
                "Status code failed for fetching users list"
        );

        String responseBody = response.getBody().asString();

        logger.info("Response: " + responseBody);

        assertTrue(
                responseBody.contains(
                        PROP_LIST.get("userservice.search.user.id").toString()
                ),
                "Expected User ID not found in response."
        );

        logger.info("Verified user ID is present in response.");
    }

    @Test
    public void testGetListOfUsersByInvalidId() throws URISyntaxException {

        logger.info("Started calling Search User API with invalid ID");

        URI endpoint = new URI(
                PROP_LIST.get("userservice.search.user.endpoint").toString()
        );

        String requestBody = "{\n" + "  \"user_ids\": [" + PROP_LIST.get("userservice.search.user.invalid.id").toString() + "]\n" + "}";

        Response response = given()
                .header(
                        "X-auth-token",
                        CommonUtilityUserService.UserToken
                )
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(endpoint);

        assertEquals(
                response.getStatusCode(),
                200,
                "Status code failed for invalid user ID request"
        );

        String responseBody = response.getBody().asString();

        logger.info("Response: " + responseBody);

        assertTrue(
                responseBody.contains("result"),
                "Result field not present in response."
        );

        logger.info("Verified response for invalid user ID.");
    }
}