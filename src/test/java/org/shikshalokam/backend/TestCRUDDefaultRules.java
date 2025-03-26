package org.shikshalokam.backend;

import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class TestCRUDDefaultRules extends MentorEDBaseTest {
    public static final Logger logger = LogManager.getLogger(TestCRUDUserModules.class);
    private String ID;

    @Test(description = "Test case to create default rules", priority = 1)
    public void testCreateDefaultRules() {
        logger.info("logging in with org admin credentials");
        loginToMentorED(PropertyLoader.PROP_LIST.getProperty("mentorED.qa.default.org.admin.email"), PropertyLoader.PROP_LIST.getProperty("mentorED.qa.default.org.admin.password"));
        Response response = createDefaultRules("mentor", "designation", false, "designation", "overlap");
        Assert.assertEquals(response.getStatusCode(), 201);
        ID = response.jsonPath().getString("result.id");
        logger.info("Validations related to creating the default rules is completed");
    }

    @Test(description = "Test Case to read the default rules", priority = 2)
    public void testReadDefaultRules() {
        Response response = readDefaultRules();
        Assert.assertEquals(response.getStatusCode(), 200, "Status code incorrect");
        Assert.assertEquals(response.jsonPath().getString("result.data[0].id"), ID, "The Default rule is not created");
        logger.info("Validations related to fetching the default rules is completed");
    }

    @Test(description = "Test case to update the default rules", priority = 3)
    public void testUpdateDefaultRules() {
        Response response = updateDefaultRules("session", "categories", false, "designation", "overlap");
        Assert.assertEquals(response.getStatusCode(), 202);
        Assert.assertEquals(response.jsonPath().getString("result.target_field"), "[categories]");
        logger.info("Validation related to updating the Default rules is completed");
    }

    @Test(description = "Test case to delete the default rules", priority = 4)
    public void testDeleteDefaultRule() {
        Response response = deleteDefaultRules();
        Assert.assertEquals(response.getStatusCode(), 202);
        Assert.assertEquals(response.jsonPath().getString("message"), "The default rule has been deleted successfully.");
    }
}