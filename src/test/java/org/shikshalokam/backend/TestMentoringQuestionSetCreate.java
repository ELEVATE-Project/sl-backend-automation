package org.shikshalokam.backend;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.net.URI;
import java.net.URISyntaxException;

import static io.restassured.RestAssured.given;
import static org.shikshalokam.backend.PropertyLoader.PROP_LIST;
import static org.testng.Assert.assertEquals;

public class TestMentoringQuestionSetCreate extends MentorEDBaseTest{

    private static final Logger logger = LogManager.getLogger(TestMentoringQuestionSetCreate.class);
    public static URI createQuestionSetEndpoint;

    @BeforeTest
    public void init() {
        logger.info("Logging into the application :");
        loginToMentorED(PROP_LIST.get("mentor.qa.admin.login.user").toString(), PROP_LIST.get("mentor.qa.admin.login.password").toString());
    }

    @Test
    public void testMentoringQuestionSetCreate() {

        logger.info("Started calling the testMentoringQuestionSetCreate:");
        try {
            createQuestionSetEndpoint = new URI("mentoring/v1/questionsSet/create");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        String requestBody="{\n" +
                "  \"questions\": [\n" +
                "    5\n" +
                "  ],\n" +
                "  \"code\": \"feedback\"\n" +
                "}";
        Response responce = given()
                .header("X-auth-token", "bearer " + X_AUTH_TOKEN)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when().post(createQuestionSetEndpoint);
        logger.info(responce.getBody().asString());

        logger.info("Ended calling the testMentoringQuestionSetCreate:");

        assertEquals(responce.getStatusCode(),201,"status code failed for testMentoringQuestionSetCreate");

    }


}
