package org.shikshalokam.backend.scp;

import static io.restassured.RestAssured.given;
import static org.shikshalokam.backend.PropertyLoader.*;
import static org.testng.Assert.*;

import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.net.URI;
import java.net.URISyntaxException;

public class TestGetReviewerList extends SelfCreationPortalBaseTest {
    private static final Logger logger = LogManager.getLogger(TestGetReviewerList.class);

    @BeforeTest
    public void init() {
        logger.info("Logging into the application:");
        loginToScp(
                PROP_LIST.get("scp.getreviewer.id").toString(),
                PROP_LIST.get("scp.getreviewer.pass").toString()
        );
    }

    @Test
    public void testGetReviewerList() {
        logger.info("Started calling the reviewerList API:");
        URI endpoint;
        try {
            endpoint = new URI("https://scp-qa.elevate-apis.shikshalokam.org/scp/v1/projects/reviewerList");
        } catch (URISyntaxException e) {
            throw new RuntimeException("Error constructing URI for reviewerList API", e);
        }

        Response response = given()
                .header("X-auth-token", "bearer " + X_AUTH_TOKEN)
                .when()
                .get(endpoint);

        assertEquals(response.getStatusCode(), 200, "Status code failed for fetching reviewerList");

        String responseBody = response.getBody().asString();
        logger.info("Response: " + responseBody);

        // Retrieve the expected email from the properties file with a null check
        String expectedEmail = PROP_LIST.getProperty("scp.getreviewer.list.verify");
        if (expectedEmail == null) {
            throw new RuntimeException("Property 'scp.getreviewer.list.verify' is missing in the properties file.");
        }

        // Verify if the response contains the expected email
        assertTrue(responseBody.contains(expectedEmail),
                "Expected email '" + expectedEmail + "' not found in the response.");

        logger.info("Verified that the expected email '" + expectedEmail + "' is present in the response.");
    }
}
