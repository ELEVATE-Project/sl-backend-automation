package org.shikshalokam.backend.userservice;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.shikshalokam.backend.PropertyLoader;
import org.shikshalokam.backend.userServiceUtility.CommonUtilityUserService;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.HashMap;

import static io.restassured.RestAssured.given;

public class TestUsersBulkUpload extends UserServiceBaseTest {

    private static final Logger logger = LogManager.getLogger(TestUsersBulkUpload.class);

    private String adminToken;

    @BeforeMethod
    public void setup() {

        String baseUrl = PropertyLoader.PROP_LIST.getProperty("userservice.qa.api.base.url");

        Assert.assertNotNull(baseUrl, "Base URL Is Null In automation.properties");

        RestAssured.baseURI = baseUrl;

        logger.info("Base URI : {}", RestAssured.baseURI);

        logger.info("Generating Admin Token");

        adminToken = CommonUtilityUserService.generateAdminToken();

        Assert.assertNotNull(adminToken, "Admin Token Generation Failed");

        Assert.assertFalse(adminToken.isEmpty(), "Admin Token Is Empty");

        logger.info("Admin Token Generated Successfully");
    }

    @Test(description = "Bulk Upload Users")
    public void testBulkUserUpload() throws Exception {

        // Step 1 : Generate Signed URL
        Response signedUrlResponse = CommonUtilityUserService.getSignedUrlForBulkUpload(adminToken);

        signedUrlResponse.prettyPrint();

        Assert.assertEquals(signedUrlResponse.getStatusCode(), 200, "Failed To Generate Signed URL");

        String signedUrl = signedUrlResponse.jsonPath().getString("result.signedUrl");

        String filePath = signedUrlResponse.jsonPath().getString("result.filePath");

        logger.info("Signed URL : {}", signedUrl);

        logger.info("File Path : {}", filePath);

        // Step 2 : Upload CSV File
        Response uploadResponse = CommonUtilityUserService.uploadBulkCsvFile(signedUrl);

        logger.info("Upload Status Code : {}", uploadResponse.getStatusCode());

        uploadResponse.prettyPrint();

        Assert.assertTrue(uploadResponse.getStatusCode() == 200 || uploadResponse.getStatusCode() == 201, "CSV Upload Failed");

        // Step 3 : Trigger Bulk Upload
        HashMap<String, Object> requestBody = new HashMap<>();

        requestBody.put("file_path", filePath);

        requestBody.put("upload_type", "upload");

        logger.info("Bulk Upload Request Body : {}", requestBody);

        Response bulkUploadResponse = given().header("X-auth-token", adminToken)
                .header("x-tenant-code", PropertyLoader.PROP_LIST.getProperty("userservice.bulkupload.tenant.code"))
                .header("x-org-code", PropertyLoader.PROP_LIST.getProperty("userservice.bulkupload.org.code"))
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post(PropertyLoader.PROP_LIST.getProperty("userservice.bulkupload.create.endpoint"));

        bulkUploadResponse.prettyPrint();

        Assert.assertEquals(bulkUploadResponse.getStatusCode(), 200, "Bulk User Upload Failed");

        logger.info("Bulk User Upload Completed Successfully");
    }

    @Test(dependsOnMethods = "testBulkUserUpload", description = "Validate Created Bulk Upload User Login")
    public void testValidateBulkUploadusers() throws Exception {

        String identifier = "rahulsher@yopmail.com";

        String password = "PASSword##11";

        Response loginResponse = null;

        boolean isLoginSuccessful = false;

        int maxRetries = 10;

        int retryCount = 1;

        while (retryCount <= maxRetries) {

            logger.info("Attempting Login For Created User - Retry : {}", retryCount);

            loginResponse = CommonUtilityUserService.loginCreatedBulkUser(identifier, password);

            loginResponse.prettyPrint();

            if (loginResponse.getStatusCode() == 200) {

                String accessToken = loginResponse.jsonPath().getString("result.access_token");

                if (accessToken != null && !accessToken.trim().isEmpty()) {

                    isLoginSuccessful = true;

                    logger.info("Created User Login Successful");

                    break;
                }
            }

            logger.info("Created User Login Failed. Waiting before next retry...");

            Thread.sleep(15000);

            retryCount++;
        }

        Assert.assertTrue(isLoginSuccessful, "Created User Login Failed After Retries");

        logger.info("Created User Login Validation Completed Successfully");
    }
}