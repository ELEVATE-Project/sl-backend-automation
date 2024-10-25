package org.shikshalokam.backend.ep;

import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.shikshalokam.backend.PropertyLoader;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.testng.AssertJUnit.fail;

public class TestElevateEntityTypeCRUDOperations extends ElevateProjectBaseTest {
    private Logger logger = LogManager.getLogger(TestElevateEntityTypeCRUDOperations.class);
    private String entityType_Id;


    @BeforeTest
    public void userLogin() {
        loginToElevate(PropertyLoader.PROP_LIST.getProperty("elevate.login.contentcreator"), PropertyLoader.PROP_LIST.getProperty("elevate.login.contentcreator.password"));
    }

    @Test(description = "creating valid entity type valid request body/name")
    public void testCreatingSingleEntitytype() {
        String entityType_Name = RandomStringUtils.randomAlphabetic(5);
        Response response = createEntitytype(entityType_Name);
        response.prettyPrint();
        logger.info("new = " + entityType_Name);
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(response.jsonPath().getString("message"), "ENTITY_INFORMATION_CREATED");
        logger.info("Validation with creating the valid entity type is verified");
    }

    @Test(description = "Creating an entity type with empty request fields")
    public void testInavlidCreatingSingleEntitytype() {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("name", "");
        Map<String, Object> registryDetails = new HashMap<>();
        registryDetails.put("name", "");
        requestBody.put("registryDetails", registryDetails);

        Response response = given().header("Authorization", INTERNAL_ACCESS_TOKEN).header("internal-access-token", INTERNAL_ACCESS_TOKEN).header("x-auth-token", X_AUTH_TOKEN).header("Content-type", "application/json").body(requestBody).post(BASE_URL + PropertyLoader.PROP_LIST.getProperty("elevate.qa.entitytypesinglecreate.endpoint"));
        if (response.getStatusCode() != 400) {
            fail(response.prettyPrint() + " ERROR..!!!!");
        }
        response.prettyPrint();
        Assert.assertEquals(response.getStatusCode(), 400, "The name field cannot be empty.");
        Assert.assertEquals(response.jsonPath().getString("message"), "[[location:body, param:name, msg:The name field cannot be empty., value:]]");
        logger.info("Validations with creating the invalid entity type are verified.");
    }

    @Test(description = "fetching the entity type with its name in request body")
    public void testFetchingEntitytype() {
        Response response = fetchSingleEntityType(CRUDEntityType_Name);
        response.prettyPrint();
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(response.jsonPath().getString("result[0].name"), CRUDEntityType_Name, "Entity type fetched successfully!!");
        Assert.assertEquals(response.jsonPath().getString("message"), "ENTITY_TYPES_FETCHED", "Entity type fetched successfully!!");
        logger.info("Validations with fetching the entity type is verified.");
    }

    @Test(description = "Updating the entity type using_id")
    public void testUpdatingSingleEntitytype() {
        getEntitytype_Id(CRUDEntityType_Name);
        String updated_Name = CRUDEntityType_Name + RandomStringUtils.randomAlphabetic(1);
        Response response = testUpdateEntitytype(updated_Name);
        response.prettyPrint();
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(response.jsonPath().getString("result.name"), updated_Name);
        logger.info("Entity type name is updated from: " + CRUDEntityType_Name + " to" + updated_Name + "entity type Id = " + entityType_Id);
        logger.info("Reverting the entity type name back to original");
        getEntitytype_Id(updated_Name);
        testUpdateEntitytype(CRUDEntityType_Name);
        getEntitytype_Id(CRUDEntityType_Name);
        logger.info("Validations related to updating the entity type with the _Id is verified");
    }

    @Test(description = "Fetching Entity Types List")
    public void testFetchingEntitytypeList() {
        Response response = fetchEntitytypeList();
        response.prettyPrint();
        List<String> getEntityTypename = response.jsonPath().getList("result.name");
        response.jsonPath().getString("result." + CRUDEntityType_Name);
        Assert.assertTrue(getEntityTypename.contains(CRUDEntityType_Name));
        logger.info("entity type " + CRUDEntityType_Name + " is fetched and is available in the list");
        logger.info("Validations with fetching the entity type list is verified");
    }

    //Method to create the entity type
    private Response createEntitytype(String entityTypeName) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("name", entityTypeName);
        Map<String, Object> registryDetails = new HashMap<>();
        registryDetails.put("name", entityTypeName);
        requestBody.put("registryDetails", registryDetails);
        requestBody.put("isObservable", true);
        requestBody.put("toBeMappedToParentEntities", true);
        Response response = given()
                .header("Authorization", INTERNAL_ACCESS_TOKEN)
                .header("internal-access-token", INTERNAL_ACCESS_TOKEN)
                .header("x-auth-token", X_AUTH_TOKEN)
                .header("Content-Type", "application/json")
                .body(requestBody)
                .post(BASE_URL + PropertyLoader.PROP_LIST.getProperty("elevate.qa.entitytypesinglecreate.endpoint"));
        if (response.getStatusCode() != 200) {
            fail(response.prettyPrint() + "ERROR....!!!!!!");
        }
        return response;
    }

    //Method to fetch entity type by name
    private Response fetchSingleEntityType(String name) {
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
    private void getEntitytype_Id(String entityName) {
        Response response = fetchSingleEntityType(entityName);
        response.prettyPrint();
        entityType_Id = response.jsonPath().getString("result[0]._id");
        Assert.assertEquals(response.jsonPath().getString("result[0].name"), entityName);
        logger.info("Entity type Id fetched successfully!! = " + entityType_Id);
    }

    //Method to update entity type
    private Response testUpdateEntitytype(String updatedentityTypeName) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("name", updatedentityTypeName);
        Map<String, Object> registryDetails = new HashMap<>();
        registryDetails.put("name", updatedentityTypeName);
        requestBody.put("registryDetails", registryDetails);
        requestBody.put("isObservable", true);
        requestBody.put("toBeMappedToParentEntities", true);

        Response response = given()
                .header("Authorization", INTERNAL_ACCESS_TOKEN)
                .header("internal-access-token", INTERNAL_ACCESS_TOKEN)
                .header("x-auth-token", X_AUTH_TOKEN)
                .header("Content-type", "application/json")
                .body(requestBody)
                .post(BASE_URL + PropertyLoader.PROP_LIST.getProperty("elevate.qa.updateentitytype.endpoint") + entityType_Id);
        logger.info(BASE_URL + PropertyLoader.PROP_LIST.getProperty("elevate.qa.updateentitytype.endpoint") + "/" + entityType_Id);

        if (response.getStatusCode() != 200) {
            fail(response.prettyPrint() + " ERROR..!!!!");
        }
        return response;
    }

    //Method to fetch entity list
    private Response fetchEntitytypeList() {
        Response response = given().header("Authorization", INTERNAL_ACCESS_TOKEN).header("internal-access-token", INTERNAL_ACCESS_TOKEN).header("x-auth-token", X_AUTH_TOKEN).header("Content-type", "application/json").get(BASE_URL + PropertyLoader.PROP_LIST.getProperty("elevate.qa.fetchentitypelist.endpoint"));
        //Terminating the method if the response code not 200
        if (response.getStatusCode() != 200) {
            fail(response.prettyPrint() + " ERROR..!!!!");
        }
        return response;
    }
}