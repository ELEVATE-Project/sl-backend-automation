package org.shikshalokam.backend.ep;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.shikshalokam.backend.MentorBase;
import org.shikshalokam.backend.PropertyLoader;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.shikshalokam.backend.PropertyLoader.PROP_LIST;
import static org.testng.AssertJUnit.fail;

public class ElevateProjectBaseTest extends MentorBase {
    static final Logger logger = LogManager.getLogger(ElevateProjectBaseTest.class);
    public static String X_AUTH_TOKEN = null;
    public static Response response = null;
    public static String User_ID = null;
    public static String BASE_URL = PropertyLoader.PROP_LIST.getProperty("elevate.qa.api.base.url");
    public static String INTERNAL_ACCESS_TOKEN = PropertyLoader.PROP_LIST.getProperty("elevate.internalaccesstoken");
    public static String entityType_Id = null;
    public static String entity_Id = null;
    public static String createdRoleID;
    public static String location_Id = null;

    // method to login with required parameters
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

    //Method to fetch entity type details by name
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
                .post(PropertyLoader.PROP_LIST.getProperty("elevate.qa.fetchentitytype.endpoint"));
        response.prettyPrint();
        return response;
    }

    //Method to get entity type ID
    public String getEntitytype_Id(String entityName) {
        Response response = fetchSingleEntityType(entityName);
        response.prettyPrint();
        entityType_Id = response.jsonPath().getString("result[0]._id");
        Assert.assertEquals(response.jsonPath().getString("result[0].name"), entityName);
        logger.info("Entity type Id fetched successfully!! = " + entityType_Id);
        return entityType_Id;
    }

    public Response fetchEntitydetails(String externalID) {
        JSONObject requestBody = new JSONObject();
        JSONObject query = new JSONObject();
        query.put("metaInformation.externalId", externalID);
        JSONArray projection = new JSONArray();
        projection.add("metaInformation.externalId");
        projection.add("metaInformation.name");
        projection.add("registryDetails.locationId");
        requestBody.put("query", query);
        requestBody.put("projection", projection);
        Response response = given()
                .header("Authorization", INTERNAL_ACCESS_TOKEN)
                .header("internal-access-token", INTERNAL_ACCESS_TOKEN)
                .header("x-auth-token", X_AUTH_TOKEN)
                .header("Content-Type", "application/json")
                .body(requestBody)
                .post(PropertyLoader.PROP_LIST.getProperty("elevate.qa.fetchentity.endpoint"));
        response.prettyPrint();
        return response;
    }

    public String getEntityId(String externalID) {
        Response response = fetchEntitydetails(externalID);
        entity_Id = response.jsonPath().getString("result._id").replaceAll("[\\[\\]]", "");
        Assert.assertTrue(response.asString().contains(externalID), "externalId not found");
        logger.info("Entity Id fetched successfully!! = " + entity_Id);
        return entity_Id;
    }

    public Response createUserRoleExtension(String title, String userRoleId, String entityType, String entityTypeId) {
        HashMap<String, Object> requestBody = new HashMap<>();
        requestBody.put("title", title);
        requestBody.put("userRoleId", userRoleId);
        requestBody.put("code", userRoleId);
        requestBody.put("entityTypes", List.of(new HashMap<String, String>() {{
            put("entityType", entityType);
            put("entityTypeId", entityTypeId);
        }}));
        Response response = given()
                .header("X-auth-token", X_AUTH_TOKEN)
                .header("internal-access-token", INTERNAL_ACCESS_TOKEN)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when().post(PROP_LIST.getProperty("createUserRoleExtensionEndpoint"));
        response.prettyPrint();
        createdRoleID = response.jsonPath().getString("result._id");
        return response;
    }

    public Response deleteUserRoleExtension(String roleID) {
        Response response = given()
                .header("X-auth-token", X_AUTH_TOKEN)
                .header("internal-access-token", INTERNAL_ACCESS_TOKEN)
                .contentType(ContentType.JSON)
                .when().pathParam("_id", roleID).delete(PROP_LIST.getProperty("deleteUserRoleExtensionEndpoint") + "{_id}"); // Use the updated URL here
        response.prettyPrint();
        return response;
    }

    public String getLocationID(String externalID) {
        Response response = fetchEntitydetails(externalID);
        location_Id = response.jsonPath().getString("result[0].registryDetails.locationId").replaceAll("[\\[\\]]", "");
        Assert.assertTrue(response.asString().contains(externalID), "locationId not found");
        logger.info("Location Id fetched successfully!! = " + location_Id);
        return location_Id;
    }
}