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

public class TestMentoringQuestionsRead extends MentorEDBaseTest{

    private static final Logger logger = LogManager.getLogger(TestMentoringQuestionsRead.class);
    private static URI mentoringQuestionsRead;

    @BeforeTest
    public void init() {
        logger.info("Logging into the application :");
        loginToMentorED(PROP_LIST.get("mentor.qa.admin.login.user").toString(), PROP_LIST.get("mentor.qa.admin.login.password").toString());
    }

    @Test
    public void testMentoringQuestoinsRead() {

        logger.info("Started calling the testMentoringQuestoinsRead:");
        String QuestionId ="14";
        try {
            mentoringQuestionsRead = new URI("mentoring/v1/questions/read");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        Response responce = given()
                .header("X-auth-token", "bearer " + X_AUTH_TOKEN)
                .pathParam("id",QuestionId)
                .contentType(ContentType.JSON)
                .when().get(mentoringQuestionsRead+"/{id}");
        logger.info(responce.getBody().asString());

        logger.info("Ended calling the testMentoringQuestoinsRead:");

    }

}
