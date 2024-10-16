package org.shikshalokam.backend.ep;

import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TestElevateBulkUploadEntityTypeUpdate extends TestElevateBulkUploadEntityTypeCreate {
    static final Logger logger = LogManager.getLogger(TestElevateBulkUploadEntityTypeUpdate.class);


    @Test(description = "Test case to update the entity using valid CSV")
    public void testValidCsv() {
        Response response = TestElevateBulkUploadEntityTypeUpdate.testUploadCsvFile("src/main/resources/config/bulk_upload_enitytype_update_valid_ElevateProject.csv", "elevate.qa.bulkenitytype.update.endpoint");
        int Statuscode = response.getStatusCode();
        Assert.assertEquals(Statuscode, 200);
        logger.info("Validations related to valid CSV bulk upload entity file is verified");
    }

    //This negative scenario cannot be verified at the moment because of the below issue
    //https://katha.shikshalokam.org/bug-view-1971.html

//    @Test(description = "Test case to update the entity using Invalid CSV")
//    public void testInValidCsv() {
//        Response response = TestElevateBulkUploadEntityUpdate.testUploadCsvFile("src/main/resources/config/bulk_upload_enitytype_update_invalid_ElevateProject.csv", "elevate.qa.bulkenitytype.update.endpoint");
//        int Statuscode = response.getStatusCode();
//        Assert.assertEquals(Statuscode, 400);
//        logger.info("Validations related to Invalid CSV bulk upload entity file is verified");
//    }
}



