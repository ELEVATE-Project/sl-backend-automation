package org.shikshalokam.backend.userservice;

import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.shikshalokam.backend.userServiceUtility.CommonUtilityUserService;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class TestUserServiceEntityOperations extends UserServiceBaseTest {

    private static final Logger logger = LogManager.getLogger(TestUserServiceEntityOperations.class);

    private String adminToken;

    private int entityId;

    private int entityTypeId;

    private final String randomValue = "ml" + RandomStringUtils.randomAlphabetic(4).toLowerCase();

    private final String randomLabel = "Malayalam" + RandomStringUtils.randomAlphabetic(3);

    private final String updatedLabel = "EnglishUpdated";

    @BeforeMethod
    public void setup() {

        logger.info("Generating Admin Token");

        adminToken = CommonUtilityUserService.generateAdminToken();

        Assert.assertNotNull(adminToken, "Admin Token Is Null");
    }

    @Test(description = "Create Entity")
    public void testCreateEntity() {

        String requestBody = "{" + "\"value\":\"" + randomValue + "\"," + "\"label\":\"" + randomLabel + "\"," + "\"status\":\"ACTIVE\"," + "\"type\":\"SYSTEM\"," + "\"entity_type_id\":319" + "}";

        logger.info("Create Entity Request Body : {}", requestBody);

        Response response = CommonUtilityUserService.createEntity(adminToken, requestBody);

        response.prettyPrint();

        Assert.assertEquals(response.getStatusCode(), 201, "Entity Creation Failed");

        entityId = response.jsonPath().getInt("result.id");

        entityTypeId = response.jsonPath().getInt("result.entity_type_id");

        logger.info("Actual Created Entity Id : {}", entityId);

        logger.info("Actual Created EntityType Id : {}", entityTypeId);

        Assert.assertTrue(entityId > 0, "Invalid Entity Id");

        Assert.assertTrue(entityTypeId > 0, "Invalid EntityType Id");
    }

    @Test(dependsOnMethods = "testCreateEntity", description = "Read Entity")
    public void testReadEntity() {

        Assert.assertTrue(entityId > 0, "Invalid Entity Id");

        logger.info("Reading Entity Using Actual Created Id : {}", entityId);

        Response response = CommonUtilityUserService.readEntity(adminToken, entityId);

        response.prettyPrint();

        Assert.assertEquals(response.getStatusCode(), 200, "Read Entity Failed");

        String actualIdString = response.jsonPath().getString("result.id");

        actualIdString = actualIdString.replace("[", "").replace("]", "").trim();

        int actualId = Integer.parseInt(actualIdString);

        String actualValue = response.jsonPath().getString("result.value");

        actualValue = actualValue.replace("[", "").replace("]", "").trim();

        Assert.assertEquals(actualId, entityId);

        Assert.assertEquals(actualValue, randomValue);

        logger.info("Entity Read Successful");
    }

    @Test(dependsOnMethods = "testCreateEntity", description = "Update Entity")
    public void testUpdateEntity() {

        Assert.assertTrue(entityId > 0, "Invalid Entity Id");

        String updateRequestBody = "{" + "\"label\":\"" + updatedLabel + "\"," + "\"status\":\"ACTIVE\"," + "\"type\":\"SYSTEM\"" + "}";

        logger.info("Update Entity Request Body : {}", updateRequestBody);

        logger.info("Updating Entity Id : {}", entityId);

        Response response = CommonUtilityUserService.updateEntity(adminToken, entityId, updateRequestBody);

        response.prettyPrint();

        Assert.assertEquals(response.getStatusCode(), 202, "Update Entity Failed");

        logger.info("Entity Updated Successfully");
    }

    @Test(dependsOnMethods = {"testCreateEntity","testReadEntity", "testUpdateEntity"}, description = "Delete Entity")
    public void testDeleteEntity() {

        Assert.assertTrue(entityId > 0, "Invalid Entity Id");

        logger.info("Deleting Entity Id : {}", entityId);

        Response response = CommonUtilityUserService.deleteEntity(adminToken, entityId);

        response.prettyPrint();

        Assert.assertEquals(response.getStatusCode(), 202, "Delete Entity Failed");

        logger.info("Entity Deleted Successfully");
    }
}