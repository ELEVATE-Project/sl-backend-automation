package org.shikshalokam.backend.ep;

import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.shikshalokam.backend.PropertyLoader;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;

public class TestElevateBulkEntityCRUDOperations extends ElevateProjectBaseTest {
    private static final Logger logger = LogManager.getLogger(TestElevateBulkEntityCRUDOperations.class);
    private List<String> randomEntityName = new ArrayList<>();
    private List<String> randomEntityId = new ArrayList<>();

    @BeforeMethod
    public void userLogin() {
        loginToElevate(PropertyLoader.PROP_LIST.getProperty("elevate.login.contentcreator"), PropertyLoader.PROP_LIST.getProperty("elevate.login.contentcreator.password"));
    }

    @Test
    public void testValidEntityBulkCreate() {
        updateCsvWithName("src/main/resources/bulk_entity_valid_create_ElevateProject.csv", "target/classes/bulk_entity_valid_create_ElevateProject.csv");
        updateCsvWithEntityId("target/classes/bulk_entity_valid_create_ElevateProject.csv", "target/classes/bulk_entity_valid_create_ElevateProject.csv");
        Response response = testUploadCsvFile("target/classes/bulk_entity_valid_create_ElevateProject.csv", "elevate.qa.bulkentity.create", PropertyLoader.PROP_LIST.getProperty("elevate.qa.automation.entitytype.name"));
        Assert.assertEquals(response.getStatusCode(), 200);
        logger.info("Validations related to valid CSV bulk upload entity file is verified");
    }

    //This test case has an issue hence we cannot use this TC until the fix is been provided by the devs
    @Test
    public void testInvalidEntityBulkCreate() {
        Response response = testUploadCsvFile("src/main/resources/bulk_entity_invalid_create_ElevateProject.csv", "elevate.qa.bulkentity.create", PropertyLoader.PROP_LIST.getProperty("elevate.qa.automation.entitytype.name"));
        Assert.assertEquals(response.getStatusCode(), 400);
        logger.info("Validations related to Invalid CSV bulk upload entity file is verified");
    }

    private Response testUploadCsvFile(String path, String endpoint, String EntityType) {
        // Path to the CSV file
        File csvFile = new File(path);
        if (!csvFile.exists()) {
            logger.error("CSV file does not exist: " + csvFile.getAbsolutePath());
        }
        // Send the POST request to upload the CSV file
        Response response = given().multiPart("entities", csvFile)
                .header("internal-access-token", INTERNAL_ACCESS_TOKEN)  // Internal access token header
                .header("x-auth-token", X_AUTH_TOKEN)  // x-authenticated-token header
                .header("Content-Type", "multipart/form-data")  // Content-Type for file upload
                .queryParam("type", EntityType)
                .post(PropertyLoader.PROP_LIST.getProperty(endpoint));
        logger.info("Response Status Code: " + response.getStatusCode());
        response.prettyPrint();
        return response;
    }

    private void updateCsvWithName(String sourcePath, String targetPath) {
        try {
            String CSVcontent = new String(Files.readAllBytes(Paths.get(sourcePath)));
            logger.info("Original Content:\n" + CSVcontent);
            randomEntityName = Stream.generate(() -> RandomStringUtils.randomAlphabetic(10))
                    .limit(3)
                    .collect(Collectors.toList());
            for (String EntityName : randomEntityName) {
                CSVcontent = CSVcontent.replaceFirst("EntityName", EntityName);
            }
            Files.write(Paths.get(targetPath), CSVcontent.getBytes());
            logger.info("Updated Content:\n" + CSVcontent);
        } catch (IOException e) {
            logger.info(e + "Error!!!");
        }
    }

    private void updateCsvWithEntityId(String sourcePath, String targetPath) {
        try {
            String CSVcontent = new String(Files.readAllBytes(Paths.get(sourcePath)));
            logger.info("Original Content:\n" + CSVcontent);
            randomEntityId = (Stream.generate(() -> RandomStringUtils.randomAlphabetic(10))
                    .limit(3)
                    .collect(Collectors.toList()));
            for (String EntityId : randomEntityId) {
                CSVcontent = CSVcontent.replaceFirst("EntityId", String.valueOf(EntityId));
            }
            Files.write(Paths.get(targetPath), CSVcontent.getBytes());
            logger.info("Updated Content:\n" + CSVcontent);
        } catch (IOException e) {
            logger.info(e + "Error!!!");
        }
    }
}