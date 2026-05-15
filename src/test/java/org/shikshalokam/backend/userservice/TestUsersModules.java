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

public class TestUsersModules extends UserServiceBaseTest {

    public static final Logger logger =
            LogManager.getLogger(TestUsersModules.class);

    private URI readUserEndpoint;
    private URI profileByIdEndpoint;
    private URI updateUserEndpoint;

    @BeforeMethod
    public void init() throws URISyntaxException {

        logger.info("Logging into User Service");

        // Use SAME admin user as Postman
        CommonUtilityUserService.loginToUser(
                PROP_LIST.get("userservice.qa.phone.login.identifier").toString(),
                PROP_LIST.get("userservice.qa.phone.login.password").toString()
        );

        readUserEndpoint = new URI(
                PROP_LIST.get("userservice.read.user.endpoint").toString()
        );

        profileByIdEndpoint = new URI(
                PROP_LIST.get("userservice.profilebyid.endpoint").toString()
                        + PROP_LIST.get("userservice.user.id").toString()
                        + "?tenant_code=default"
        );

        updateUserEndpoint = new URI(
                PROP_LIST.get("userservice.update.user.endpoint").toString()
        );
    }

    @Test
    public void testReadUser() {

        logger.info("Started calling Read User API");

        Response response = given()
                .header(
                        "X-auth-token",
                        CommonUtilityUserService.UserToken
                )
                .when()
                .get(readUserEndpoint);

        response.prettyPrint();

        assertEquals(
                response.getStatusCode(),
                200,
                "Read User API failed"
        );

        logger.info("Successfully verified Read User API");
    }

    @Test
    public void testGetUserProfileById() {

        logger.info("Started calling Profile By ID API");

        Response response = given()
                .header(
                        "internal_access_token",
                        PROP_LIST.get("userservice.internal.access.token").toString()
                )
                .when()
                .get(profileByIdEndpoint);

        response.prettyPrint();

        assertEquals(
                response.getStatusCode(),
                200,
                "Profile By ID API failed"
        );

        assertTrue(
                response.getBody().asString().contains(
                        PROP_LIST.get("userservice.user.id").toString()
                ),
                "Expected User ID not found in response"
        );

        logger.info("Successfully verified Profile By ID API");
    }

    @Test
    public void testUpdateUser() {

        logger.info("Started calling Update User API");

        // RAW JSON EXACTLY LIKE POSTMAN
        String requestBody =
                "{\n" +
                        "    \"name\": \"" +
                        PROP_LIST.get("userservice.update.user.name") +
                        "\",\n" +

                        "    \"location\": \"" +
                        PROP_LIST.get("userservice.update.user.location") +
                        "\",\n" +

                        "    \"about\": \"" +
                        PROP_LIST.get("userservice.update.user.about") +
                        "\",\n" +

                        "    \"has_accepted_terms_and_conditions\": true,\n" +

                        "    \"image\": \"" +
                        PROP_LIST.get("userservice.update.user.image") +
                        "\",\n" +

                        "    \"state\": \"" +
                        PROP_LIST.get("userservice.update.user.state") +
                        "\",\n" +

                        "    \"roles\": [\n" +
                        "        " +
                        PROP_LIST.get("userservice.update.user.role") +
                        "\n" +
                        "    ],\n" +

                        "    \"district\": \"" +
                        PROP_LIST.get("userservice.update.user.district") +
                        "\",\n" +

                        "    \"block\": \"" +
                        PROP_LIST.get("userservice.update.user.block") +
                        "\",\n" +

                        "    \"cluster\": \"" +
                        PROP_LIST.get("userservice.update.user.cluster") +
                        "\",\n" +

                        "    \"school\": \"" +
                        PROP_LIST.get("userservice.update.user.school") +
                        "\",\n" +

                        "    \"professional_role\": \"" +
                        PROP_LIST.get("userservice.update.user.professional.role") +
                        "\",\n" +

                        "    \"professional_subroles\": [\n" +
                        "        \"" +
                        PROP_LIST.get("userservice.update.user.professional.subrole") +
                        "\"\n" +
                        "    ]\n" +
                        "}";

        logger.info("Request Body : " + requestBody);

        Response response = given()
                .header(
                        "X-auth-token",
                        CommonUtilityUserService.UserToken
                )
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .patch(updateUserEndpoint);

        response.prettyPrint();

        assertEquals(
                response.getStatusCode(),
                200,
                "Update User API failed"
        );

        assertTrue(
                response.getBody().asString().contains(
                        PROP_LIST.get("userservice.update.user.name").toString()
                ),
                "Updated username not found in response"
        );

        logger.info("Successfully verified Update User API");
    }
}