package org.shikshalokam.backend.userservice;

import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.shikshalokam.backend.PropertyLoader;
import org.shikshalokam.backend.userServiceUtility.CommonUtilityUserService;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class TestUserServiceEntityTypeOperations extends UserServiceBaseTest {

    private static final Logger logger = LogManager.getLogger(TestUserServiceEntityTypeOperations.class);

    private String adminToken;

    private int entityTypeId;

    private final String randomValue = "ln" + RandomStringUtils.randomAlphabetic(4).toLowerCase();

    private final String randomLabel = "Languages" + RandomStringUtils.randomAlphabetic(3);

    @BeforeMethod
    public void setup() {

        adminToken = CommonUtilityUserService.generateAdminToken();
    }

    @Test(description = "Create Entity Type")
    public void testCreateEntityType() {

        String createRequestBody = "{" + "\"value\":\"" + randomValue + "\"," + "\"label\":\"" + randomLabel + "\"," + "\"status\":\"" + PropertyLoader.PROP_LIST.getProperty("userservice.entitytype.status") + "\"," + "\"type\":\"" + PropertyLoader.PROP_LIST.getProperty("userservice.entitytype.type") + "\"," + "\"allow_filtering\":true," + "\"model_names\":[\"" + PropertyLoader.PROP_LIST.getProperty("userservice.entitytype.modelname") + "\"]," + "\"data_type\":\"" + PropertyLoader.PROP_LIST.getProperty("userservice.entitytype.datatype") + "\"" + "}";

        logger.info("Create Entity Type Request Body : {}", createRequestBody);

        Response response = CommonUtilityUserService.createEntityType(adminToken, createRequestBody);

        response.prettyPrint();

        Assert.assertEquals(response.getStatusCode(), 201, "Entity Type Creation Failed");

        entityTypeId = response.jsonPath().getInt("result.id");

        Assert.assertTrue(entityTypeId > 0, "Invalid Entity Type Id Generated");

        logger.info("Entity Type Created Successfully");
    }

    @Test(dependsOnMethods = "testCreateEntityType", description = "Read Created Entity Type")
    public void testReadEntityType() {

        String readRequestBody = "{" + "\"value\":[" + "\"" + randomValue + "\"" + "]" + "}";

        logger.info("Read Entity Type Request Body : {}", readRequestBody);

        Response response = CommonUtilityUserService.readEntityType(adminToken, readRequestBody);

        response.prettyPrint();

        Assert.assertEquals(response.getStatusCode(), 200);

    }

    @Test(dependsOnMethods = "testCreateEntityType", description = "Read All Entity Types")
    public void testReadAllEntityTypes() {

        Response response = CommonUtilityUserService.readAllEntityTypes(adminToken);

        response.prettyPrint();

        Assert.assertEquals(response.getStatusCode(), 200);

    }

    @Test(dependsOnMethods = "testCreateEntityType", description = "Delete Entity Type")
    public void testDeleteEntityType() {

        Assert.assertTrue(entityTypeId > 0, "Entity Type Id Is Invalid");

        Response response = CommonUtilityUserService.deleteEntityType(adminToken, entityTypeId);

        response.prettyPrint();

        Assert.assertEquals(response.getStatusCode(), 202, "Delete Entity Type Failed");

        logger.info("Entity Type Deleted Successfully");
    }
}

