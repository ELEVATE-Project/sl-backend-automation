package org.shikshalokam.backend.ep;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.shikshalokam.backend.MentorBase;
import org.shikshalokam.backend.PropertyLoader;
import org.testng.Assert;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.testng.AssertJUnit.fail;

public class ElevateProjectBaseTest extends MentorBase {
    static final Logger logger = LogManager.getLogger(ElevateProjectBaseTest.class);
    public static String X_AUTH_TOKEN = null;
    public static Response response = null;
    public static String User_ID = null;
    public static String BASE_URL = PropertyLoader.PROP_LIST.getProperty("elevate.qa.api.base.url");
    public static String INTERNAL_ACCESS_TOKEN = PropertyLoader.PROP_LIST.getProperty("elevate.internalaccesstoken");
    public static String CRUDEntityType_Name = PropertyLoader.PROP_LIST.getProperty("elevate.qa.CRUDEntitytype.name");
    public static String entityType_Id;

    // method to Login with required parameters
    public static Response loginToElevate(String email, String Password) {
        RestAssured.baseURI = PropertyLoader.PROP_LIST.getProperty("elevate.qa.api.base.url");
        try {
            response = given().contentType("application/x-www-form-urlencoded; charset=utf-8").params("email", email, "password", Password).post(new URI(PropertyLoader.PROP_LIST.getProperty("elevate.login.endpoint")));
            if (response == null) {
                logger.info("NO response received login to the elevate is failed hence terminating");
                System.exit(-1);
            }
            X_AUTH_TOKEN = response.body().jsonPath().get("result.access_token");
        } catch (URISyntaxException e) {
            logger.info(e.getMessage());
        }
        return response;
    }

    //Method to fetch entity type by name
    public Response fetchSingleEntityType(String name) {
        Map<String, Object> requestBody = new HashMap<>();
        Map<String, String> query = new HashMap<>();
        query.put("name", name);
        Map<String, Integer> projection = new HashMap<>();
        projection.put("_id", 1);
        projection.put("name", 1);
        Map<String, Integer> skipFields = new HashMap<>();
        skipFields.put("createdAt", 1);
        requestBody.put("query", query);
        requestBody.put("projection", projection);
        requestBody.put("skipFields", skipFields);

        Response response = given()
                .header("Authorization", INTERNAL_ACCESS_TOKEN)
                .header("internal-access-token", INTERNAL_ACCESS_TOKEN)
                .header("x-auth-token", X_AUTH_TOKEN)
                .header("Content-Type", "application/json")
                .body(requestBody) // Send the HashMap request body
                .post(BASE_URL + PropertyLoader.PROP_LIST.getProperty("elevate.qa.fetchentitytype.endpoint"));
        if (response.getStatusCode() != 200) {
            fail(response.prettyPrint() + " ERROR..!!!!");
        }
        return response;
    }

    //Method to get entity type Id
    public void getEntitytype_Id(String entityName) {
        Response response = fetchSingleEntityType(entityName);
        response.prettyPrint();
        entityType_Id = response.jsonPath().getString("result[0]._id");
        Assert.assertEquals(response.jsonPath().getString("result[0].name"), entityName);
        logger.info("Entity type Id fetched successfully!! = " + entityType_Id);
    }
}

