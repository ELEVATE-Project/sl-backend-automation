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
    private final String randomEntitytypeName = RandomStringUtils.randomAlphabetic(10);

    @BeforeTest
    public void userLogin() {
        loginToElevate(PropertyLoader.PROP_LIST.getProperty("elevate.login.contentcreator"), PropertyLoader.PROP_LIST.getProperty("elevate.login.contentcreator.password"));
    }

    @Test(description = "creating valid entity type valid request body/name")
    public void testCreatingSingleEntitytype() {
        Response response = createEntitytype(randomEntitytypeName, "true", "true");
        if (response.getStatusCode() != 200) {
            fail(response.prettyPrint() + "ERROR....!!!!!!");
        }
        response.prettyPrint();
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(response.jsonPath().getString("message"), "ENTITY_INFORMATION_CREATED");
        logger.info("new = " + randomEntitytypeName);
        logger.info("Validation with creating the valid entity type is verified");
    }

    @Test(description = "Creating an entity type with empty request fields")
    public void testInavlidCreatingSingleEntitytype() {
        Response response = createEntitytype("", "", "");
        response.prettyPrint();
        Assert.assertEquals(response.getStatusCode(), 400, "The name field cannot be empty.");
        Assert.assertEquals(response.jsonPath().getString("message"), "[[location:body, param:name, msg:The name field cannot be empty., value:]]");
        logger.info("Validations with creating the invalid entity type are verified.");
    }

    @Test(description = "fetching the entity type with its name in request body", dependsOnMethods = "testCreatingSingleEntitytype")
    public void testFetchingEntitytype() {
        Response response = fetchSingleEntityType(randomEntitytypeName);
        response.prettyPrint();
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(response.jsonPath().getString("result[0].name"), randomEntitytypeName, "Entity type fetched successfully!!");
        Assert.assertEquals(response.jsonPath().getString("message"), "ENTITY_TYPES_FETCHED", "Entity type fetched successfully!!");
        logger.info("Validations with fetching the entity type is verified.");
    }

    @Test(description = "Updating the entity type using_id", dependsOnMethods = "testCreatingSingleEntitytype")
    public void testUpdatingSingleEntitytype() {
        getEntitytype_Id(randomEntitytypeName);
        String updated_Name = randomEntitytypeName + RandomStringUtils.randomAlphabetic(2);
        Response response = testUpdateEntitytype(updated_Name, "true", "true");
        response.prettyPrint();
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(response.jsonPath().getString("result.name"), updated_Name);
        logger.info("Entity type name is updated from: " + randomEntitytypeName + " to" + updated_Name + "entity type Id = " + entityType_Id);
        logger.info("Validations related to updating the entity type with the _Id is verified");
    }

    @Test(description = "Fetching Entity Types List", dependsOnMethods = "testCreatingSingleEntitytype")
    public void testFetchingEntitytypeList() {
        Response response = fetchEntitytypeList();
        response.prettyPrint();
        List<String> getEntityTypename = response.jsonPath().getList("result.name");
        response.jsonPath().getString("result." + randomEntitytypeName);
        Assert.assertTrue(getEntityTypename.contains(randomEntitytypeName));
        logger.info("entity type " + randomEntitytypeName + " is fetched and is available in the list");
        logger.info("Validations with fetching the entity type list is verified");
    }

    //Method to create the entity type
    private Response createEntitytype(String entityTypeName, String isObservable, String toBeMappedToParentEntities) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("name", entityTypeName);
        Map<String, Object> registryDetails = new HashMap<>();
        registryDetails.put("name", entityTypeName);
        requestBody.put("registryDetails", registryDetails);
        requestBody.put("isObservable", isObservable);
        requestBody.put("toBeMappedToParentEntities", toBeMappedToParentEntities);
        Response response = given()
                .header("Authorization", INTERNAL_ACCESS_TOKEN)
                .header("internal-access-token", INTERNAL_ACCESS_TOKEN)
                .header("x-auth-token", X_AUTH_TOKEN)
                .header("Content-Type", "application/json")
                .body(requestBody)
                .post(BASE_URL + PropertyLoader.PROP_LIST.getProperty("elevate.qa.entitytypesinglecreate.endpoint"));
        return response;
    }

    //Method to update entity type
    private Response testUpdateEntitytype(String updatedentityTypeName, String isObservable, String toBeMappedToParentEntities) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("name", updatedentityTypeName);
        Map<String, Object> registryDetails = new HashMap<>();
        registryDetails.put("name", updatedentityTypeName);
        requestBody.put("registryDetails", registryDetails);
        requestBody.put("isObservable", isObservable);
        requestBody.put("toBeMappedToParentEntities", toBeMappedToParentEntities);

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