package org.shikshalokam.backend.userservice;

import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

        Response response =
                CommonUtilityUserService.createEntityType(
                        adminToken,
                        randomValue,
                        randomLabel
                );

        response.prettyPrint();

        Assert.assertEquals(
                response.getStatusCode(),
                201,
                "Entity Type Creation Failed"
        );

        Assert.assertTrue(
                response.getBody()
                        .asString()
                        .contains(randomValue)
        );

        Assert.assertTrue(
                response.getBody()
                        .asString()
                        .contains(randomLabel)
        );

        entityTypeId =
                response.jsonPath()
                        .getInt("result.id");

        Assert.assertTrue(
                entityTypeId > 0,
                "Invalid Entity Type Id Generated"
        );

        logger.info(
                "Created Entity Type Id : {}",
                entityTypeId
        );
    }

    @Test(dependsOnMethods = "testCreateEntityType", description = "Read Created Entity Type")
    public void testReadEntityTypes() {

        Response response = CommonUtilityUserService.readEntityType(adminToken, randomValue);

        response.prettyPrint();

        Assert.assertEquals(response.getStatusCode(), 200);

        logger.info("Entity Type Read Successful");
    }

    @Test(dependsOnMethods = "testCreateEntityType", description = "Read All Entity Types")
    public void testReadAllEntityTypesWithEntities() {

        Response response = CommonUtilityUserService.readAllEntityTypes(adminToken);

        response.prettyPrint();

        Assert.assertEquals(response.getStatusCode(), 200);

        logger.info("Entity Type Available In List");
    }

    @Test(dependsOnMethods = "testCreateEntityType", description = "Delete Entity Type")
    public void testDeleteEntityType() {

        Response response = CommonUtilityUserService.deleteEntityType(adminToken, entityTypeId);

        response.prettyPrint();

        Assert.assertEquals(response.getStatusCode(), 202, "Delete Entity Type Failed");

        logger.info("Entity Type Deleted Successfully");
    }
}