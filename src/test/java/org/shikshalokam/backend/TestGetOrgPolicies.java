package org.shikshalokam.backend;


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


public class TestGetOrgPolicies extends MentorEDBaseTest {
    private static final Logger logger = LogManager.getLogger(TestGetOrgPolicies.class);

    @BeforeTest
    public void init() {
        logger.info("Logging into the application :");
        loginToMentorED(PROP_LIST.get("mentor.qa.admin.login.user").toString(), PROP_LIST.get("mentor.qa.admin.login.password").toString());
    }

    @Test
    public void testGetOrgPolicies() {

        logger.info("Started calling the get org Policies:");
        URI endpoint;
        try {
            endpoint = new URI("mentoring/v1/org-admin/getOrgPolicies");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        Response responce = given().header("X-auth-token", "bearer " + X_AUTH_TOKEN).when().get(endpoint);
        logger.info(responce.getBody().asString());
        assertEquals(responce.getStatusCode(),200,"status code failed for fetching getOrgPolicies");
        assertEquals(responce.getBody().jsonPath().get("result.session_visibility_policy"),"CURRENT","session_visibility_policy Not valid");
        assertEquals(responce.getBody().jsonPath().get("result.mentor_visibility_policy"),"CURRENT","mentor_visibility_policy Not valid");

        logger.info("Ended calling the get org Policies: with all the assertions");

    }
}
