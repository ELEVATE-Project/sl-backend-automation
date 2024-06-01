package org.shikshalokam.backend;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;

import static io.restassured.RestAssured.given;

public class UpdateForm extends MentorEDBaseTest{

    private static final Logger logger = LogManager.getLogger(UpdateForm.class);
    private String updateFormEndpoint = "mentoring/v1/form/update";

    public void updateForm (String orgAdminUser, String password,JSONObject readFormPayload)
    {
        logger.info("Started calling the UpdateForm:");
        loginToMentorED(orgAdminUser, password);
        URI updateFormEndpointURI = null;
        try {
            updateFormEndpointURI = new URI(updateFormEndpoint);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        Response response = given()
                .header("X-auth-token", "bearer " + X_AUTH_TOKEN)
                .contentType(ContentType.JSON)
                .body(readFormPayload)
                .when().put(updateFormEndpointURI);

        logger.info("UpdateFrom response body : " + response.getBody().asString());

    }


}
