package org.shikshalokam.backend.ep;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.shikshalokam.backend.PropertyLoader;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;

import static io.restassured.RestAssured.given;

public class TestElevateBulkUploadEntityTypeCreate extends ElevateProjectBaseTest {
    static final Logger logger = LogManager.getLogger(TestElevateBulkUploadEntityTypeCreate.class);

    @Test(description = "Test case to create entity using valid CSV")
    public void testValidCsv() {
        Response response = TestElevateBulkUploadEntityTypeCreate.testUploadCsvFile("src/main/resources/config/bulk_upload_entity_type_create_ElevateProject.csv", "elevate.qa.bulkenitytype.create.endpoint");
        int Statuscode = response.getStatusCode();
        Assert.assertEquals(Statuscode, 200);
        //Assert.assertEquals(response.jsonPath().getString("SUCCESS"), "SUCCESS");
        logger.info("Validations related to valid CSV bulk upload entity file is verified");
    }

    //This negative scenario cannot be verified at the moment because of the below issue
    //https://katha.shikshalokam.org/bug-view-1971.html

//    @Test(description = "Test case to create entity using Invalid CSV")
//    public void testInValidCsv() {
//        Response response = TestElevateBulkUploadEntityCreate.testUploadCsvFile("src/main/resources/bulk_upload_entity_type_create_ElevateProject.csv", "elevate.qa.bulkenitytype.create.endpoint");
//        int Statuscode = response.getStatusCode();
//        Assert.assertEquals(Statuscode, 400);
//        //Assert.assertEquals(response.jsonPath().getString("status"), "FAILURE");
//        logger.info("Validations related to Invalid CSV bulk upload entity file is verified");
//    }

    //Method to upload CSV file path and endpoint is dynamic so that it can be called and re-used in future
    public static Response testUploadCsvFile(String path, String endpoint) {
        ElevateProjectBaseTest.loginToElevate("manju@guerrillamail.info", "Password@123");
        logger.info("X_AUTH_TOKEN: " + X_AUTH_TOKEN);
        try {
            RestAssured.baseURI = PropertyLoader.PROP_LIST.getProperty("elevate.qa.api.base.url");
            // Path to the CSV file
            File csvFile = new File(path);
            if (!csvFile.exists()) {
                logger.error("CSV file does not exist: " + csvFile.getAbsolutePath());
            }

            // Send the POST request to upload the CSV file
            Response response = given().multiPart("entityTypes", csvFile)
                    .header("Authorization", internalAccessToken)  // Authentication token
                    .header("internal-access-token", internalAccessToken)  // Internal access token header
                    .header("x-authenticated-token", X_AUTH_TOKEN)  // x-authenticated-token header
                    .header("Content-Type", "multipart/form-data")  // Content-Type for file upload
                    .post(PropertyLoader.PROP_LIST.getProperty(endpoint));
            logger.info("Response Status Code: " + response.getStatusCode());
            response.prettyPrint();
            if (response.getStatusCode() != 200){
                logger.info(response.getStatusCode() + "Unexpected status code please check/update the CSV file");
                System.exit(-1);
            }

        } catch (Exception e) {
            logger.error("Error occurred while uploading CSV file: ", e);
            throw new RuntimeException(e);
        }
        return response;
    }

}



