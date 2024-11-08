package org.shikshalokam.backend;

import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.net.URI;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.shikshalokam.backend.PropertyLoader.PROP_LIST;
import static org.testng.Assert.*;

public class TestSessionCRUD extends MentorEDBaseTest {
    private static final Logger logger = LogManager.getLogger(TestSessionCRUD.class);
    private URI getSessionList, getEnrolledSessionList;

    @BeforeMethod
    public void init() {
        logger.info("Logging into the application :");
        loginToMentorED(PROP_LIST.get("mentor.qa.admin.login.user").toString(),
                PROP_LIST.get("mentor.qa.admin.login.password").toString());
        getSessionList = MentorBase.createURI("mentoring/v1/mentees/homeFeed");
        getEnrolledSessionList = MentorBase.createURI("mentoring/v1/mentees/sessions");
    }


    @Test(description = "Fetches the list of all sessions of the user")
    public void testGetListofSessions() {
        logger.info("Started calling---- Get list of sessions API");
        Response response = given()
                .header("X-auth-token", "bearer " + X_AUTH_TOKEN)
                .get(getSessionList);
        //response.prettyPrint();
        assertEquals(response.getStatusCode(), 200, "Status code failed for fetching list of sessions");
        List<Object> sessionList = response.jsonPath().getList("result.all_sessions");
        assertTrue(sessionList.size() > 0, "Expected count to be greater than zero");
        logger.info("Ended calling ---- Get list of sessions API: with assertions completed.");
    }


    @Test(description = "Fetches the list of enrolled sessions of the user")
    public void testGetListofEnrolledSessions() {
        logger.info("Started calling---- Get list of Enrolled sessions API");
        Response response = given()
                .header("X-auth-token", "bearer " + X_AUTH_TOKEN)
                .get(getEnrolledSessionList);
        //response.prettyPrint();
        assertEquals(response.getStatusCode(), 200, "Status code failed for fetching list of enrolled sessions");
        List<Object> sessionList = response.jsonPath().getList("result.data");
        assertTrue(sessionList.size() > 0, "Expected count to be greater than zero");
        logger.info("Get Enrolled Sessions list complete with assertions");
    }
}














