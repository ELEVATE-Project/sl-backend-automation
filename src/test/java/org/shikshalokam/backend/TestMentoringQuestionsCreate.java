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

public class TestMentoringQuestionsCreate extends MentorEDBaseTest {

    private static final Logger logger = LogManager.getLogger(TestMentoringQuestionsCreate.class);
    public static URI createQuestionEndpoint;

    @BeforeTest
    public void init() {
        logger.info("Logging into the application :");
        loginToMentorED(PROP_LIST.get("mentor.qa.admin.login.user").toString(), PROP_LIST.get("mentor.qa.admin.login.password").toString());
    }

    @Test
    public void testCreatingQuestion() {

        logger.info("Started calling the CreateQuestion:");
        try {
            createQuestionEndpoint = new URI("mentoring/v1/questions/create");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        String requestBody="{\n" +
                "  \"name\": \"keyvalue\",\n" +
                "  \"question\": \"To what extent did you feel comfortable sharing your thoughts in the session?\",\n" +
                "  \"type\": \"rating\",\n" +
                "  \"options\": null,\n" +
                "  \"no_of_stars\": 5,\n" +
                "  \"status\": \"active\",\n" +
                "  \"category\": null,\n" +
                "  \"rendering_data\": {\n" +
                "    \"value\": \"\",\n" +
                "    \"class\": \"ion-margin\",\n" +
                "    \"disabled\": false,\n" +
                "    \"noOfstars\": \"5\",\n" +
                "    \"position\": \"floating\",\n" +
                "    \"validation\": {\n" +
                "      \"required\": false\n" +
                "    }\n" +
                "  },\n" +
                "  \"meta\": null\n" +
                "}";
        Response responce = given()
                .header("X-auth-token", "bearer " + X_AUTH_TOKEN)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when().post(createQuestionEndpoint);
        logger.info(responce.getBody().asString());

        logger.info("Ended calling the CreateQuestion:");

    }


}
