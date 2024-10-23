package org.shikshalokam.backend.ep;

import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.shikshalokam.backend.PropertyLoader;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.testng.AssertJUnit.fail;

public class TestElevateEntityTypeCRUDOperations extends ElevateProjectBaseTest {
    private static final Logger logger = LogManager.getLogger(TestElevateEntityTypeCRUDOperations.class);
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int LENGTH = 6;  // length of entity type name (random entity)
    private static final String randomString = generateRandomString();
    private static String entityType_Id = null;
    public static String entityTypeName = randomString;
    private final String entityTypeUpdateName = entityTypeName + "xyz";

    @BeforeTest
    public void userLogin() {
        loginToElevate(PropertyLoader.PROP_LIST.getProperty("elevate.login.contentcreator"), PropertyLoader.PROP_LIST.getProperty("elevate.login.contentcreator.password"));
    }

    @Test(description = "creating valid entity type valid request body/name")
    public void testCreateValidEntitytype() {
        Response response = createValidEntitytype(entityTypeName);
        response.prettyPrint();
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(response.jsonPath().getString("message"), "ENTITY_INFORMATION_CREATED");
        logger.info("Validation with correct entity type name is verified and the entity type is created successfully...!!!!");
    }

    @Test(description = "Creating an entity type with empty request fields")
    public void testCreateInvalidEntitytype() {
        // Empty request body
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("name", "");
        Map<String, Object> registryDetails = new HashMap<>();
        registryDetails.put("name", "");
        requestBody.put("registryDetails", registryDetails);
        // POST request
        Response response = given().header("Authorization", INTERNAL_ACCESS_TOKEN).header("internal-access-token", INTERNAL_ACCESS_TOKEN).header("x-auth-token", X_AUTH_TOKEN).header("Content-type", "application/json").body(requestBody).post(BASE_URL + PropertyLoader.PROP_LIST.getProperty("elevate.qa.entitytypesinglecreate.endpoint"));
        //Terminating the method if the response code not 200
        if (response.getStatusCode() != 400) {
            fail(response.prettyPrint() + " ERROR..!!!!");
        }
        // Validations
        response.prettyPrint();
        Assert.assertEquals(response.getStatusCode(), 400, "The name field cannot be empty.");
        Assert.assertEquals(response.jsonPath().getString("message"), "[[location:body, param:name, msg:The name field cannot be empty., value:]]");
        logger.info("Validations with empty/Invalid request fields are verified.");
    }

    @Test(description = "fetching the entity type with its name in request body")
    public void testFetchEntitytype() {
        Response response = fetchEntityType(entityTypeName);
        response.prettyPrint();
        List<String> getEntitytypeName = response.jsonPath().getList("result.name");
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertTrue(getEntitytypeName.contains(entityTypeName), "The response body is empty please provide valid entity type name");
        Assert.assertEquals(response.jsonPath().getString("message"), "ENTITY_TYPES_FETCHED", "Entity type fetched successfully!!");
        logger.info("Validations with fetching the entity type is verified.");
    }

    @Test(description = "Updating the entity type using_id")
    public void testUpdateSingleEntitytypeValidFields() {
        Response responseFetch = fetchEntityType(entityTypeName);
        responseFetch.prettyPrint();
        List<String> getEntitytypeName = responseFetch.jsonPath().getList("result.name");
        List<String> getEntitytypeID = responseFetch.jsonPath().getList("result._id");
        int i = 0;
        if (getEntitytypeName.contains(entityTypeName)) {
            while (i < getEntitytypeName.size()) {
                entityTypeName = getEntitytypeName.get(i);
                entityType_Id = getEntitytypeID.get(i);
                logger.info("Entity with name : " + entityTypeName + " found. _id: " + entityType_Id);
                break;
            }
        } else {
            fail(responseFetch.prettyPrint() + "Entity type not found");
        }
        Assert.assertTrue(getEntitytypeName.contains(entityTypeName), "entity fetched successfully");

        //update entity type method call
        Response response = testUpdateEntitytype(entityTypeUpdateName);
        response.prettyPrint();
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(response.jsonPath().getString("result.name"), entityTypeUpdateName, "String message");
        logger.info("Entity type name is updated from: " + entityTypeName + " to" + entityTypeUpdateName + "entity type Id = " + entityType_Id);
        logger.info("Validations related to updating the entity type with the _Id is verified");
    }

    //This method has an issue and needs a fix from the devs - https://katha.shikshalokam.org/bug-view-1971.html
//    @Test(description = "trying to update the entity type with Invalid/empty request fields")
//    public void testUpdateEntityTypeInvalidFields() {
//        //empty request body
//        Map<String, Object> requestBody = new HashMap<>();
//        requestBody.put("name", "");
//        Map<String, Object> registryDetails = new HashMap<>();
//        registryDetails.put("name", "");
//        requestBody.put("registryDetails", registryDetails);
//        requestBody.put("isObservable", true);
//        requestBody.put("toBeMappedToParentEntities", true);
//
//        // Perform the PUT request to update the entity
//        Response response = given().header("Authorization", INTERNAL_ACCESS_TOKEN).header("internal-access-token", INTERNAL_ACCESS_TOKEN).header("x-auth-token", X_AUTH_TOKEN).header("Content-Type", "application/json").body(requestBody).post(BASE_URL + PropertyLoader.PROP_LIST.getProperty("elevate.qa.updateentitytype.endpoint") + entityType_Id);
//        if (response.getStatusCode() != 400){
//            response.prettyPrint();
//            fail("Unexpected Status code check the entity type _Id");
//        }
//        response.prettyPrint();
//        Assert.assertEquals(response.getStatusCode(), 400);
//        Assert.assertEquals(response.jsonPath().getString("msg"), "required name", "Verified with response body");
//        logger.info("Validations related to empty request body/fields while updating the entity types are verified");
//    }

    @Test(description = "Fetching Entity Types List")
    public void testFetchEntitytypeList() {
        Response response = fetchEntitytypeList();
        response.prettyPrint();
        List<String> getEntityTypename = response.jsonPath().getList("result.name");
        Assert.assertTrue(getEntityTypename.contains(entityTypeName));
        logger.info("entity type " + entityTypeName + " is fetched and is available in the list");
        logger.info("Validation related to fetching the entity types list is verified");
    }

    //Method to generate random entity names
    public static String generateRandomString() {
        Random random = new Random();
        StringBuilder newString = new StringBuilder(LENGTH);
        for (int i = 0; i < LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            newString.append(CHARACTERS.charAt(index));
        }
        return newString.toString();
    }

    //Method to create the entity type
    public Response createValidEntitytype(String entityTypeName) {
        // request body using a Map for valid entity creation
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("name", entityTypeName);
        Map<String, Object> registryDetails = new HashMap<>();
        registryDetails.put("name", entityTypeName);
        requestBody.put("registryDetails", registryDetails);
        requestBody.put("isObservable", true);
        requestBody.put("toBeMappedToParentEntities", true);
        // POST request to create an entity type
        Response response = given().header("Authorization", INTERNAL_ACCESS_TOKEN).header("internal-access-token", INTERNAL_ACCESS_TOKEN).header("x-auth-token", X_AUTH_TOKEN).header("Content-Type", "application/json").body(requestBody).post(BASE_URL + PropertyLoader.PROP_LIST.getProperty("elevate.qa.entitytypesinglecreate.endpoint"));
        if (response.getStatusCode() != 200) {
            fail(response.prettyPrint() + "ERROR....!!!!!!");
        }
        return response;
    }

    //Update Entity type name with new name using entity type ID
    public Response testUpdateEntitytype(String updatedentityTypeName) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("name", updatedentityTypeName);
        Map<String, Object> registryDetails = new HashMap<>();
        registryDetails.put("name", updatedentityTypeName);
        requestBody.put("registryDetails", registryDetails);
        requestBody.put("isObservable", true);
        requestBody.put("toBeMappedToParentEntities", true);

        // Perform the PUT request to update the entity
        Response response = given().header("Authorization", INTERNAL_ACCESS_TOKEN).header("internal-access-token", INTERNAL_ACCESS_TOKEN).header("x-auth-token", X_AUTH_TOKEN).header("Content-type", "application/json").body(requestBody).post(BASE_URL + PropertyLoader.PROP_LIST.getProperty("elevate.qa.updateentitytype.endpoint") + entityType_Id);
        //Terminating the method if the response code not 200
        if (response.getStatusCode() != 200) {
            fail(response.prettyPrint() + " ERROR..!!!!");
        }
        return response;
    }

    //Method to fetch entity type by name
    public static Response fetchEntityType(String name) {
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
                .post(BASE_URL + PropertyLoader.PROP_LIST.getProperty("elevate.qa.findentitytype.endpoint"));
        entityType_Id = response.jsonPath().getString("_id");
        if (response.getStatusCode() != 200) {
            fail(response.prettyPrint() + " ERROR..!!!!");
        }
        return response;
    }

    //Fetch entity by name, get _id
    public Response fetchEntitytypeList() {
        Response response = given().header("Authorization", INTERNAL_ACCESS_TOKEN).header("internal-access-token", INTERNAL_ACCESS_TOKEN).header("x-auth-token", X_AUTH_TOKEN).header("Content-type", "application/json").get(BASE_URL + PropertyLoader.PROP_LIST.getProperty("elevate.qa.fetchentitypelist.endpoint"));
        //Terminating the method if the response code not 200
        if (response.getStatusCode() != 200) {
            fail(response.prettyPrint() + " ERROR..!!!!");
        }
        return response;
    }
}