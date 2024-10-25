package org.shikshalokam.backend.ep;

import com.opencsv.CSVWriter;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.shikshalokam.backend.PropertyLoader;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static org.shikshalokam.backend.ep.TestElevateEntityTypeCRUDOperations.entityTypeName;
import static org.testng.AssertJUnit.fail;

public class TestElevateBulkUploadEntityTypeCRUDOperations extends ElevateProjectBaseTest {
    static final Logger logger = LogManager.getLogger(TestElevateBulkUploadEntityTypeCRUDOperations.class);
    private static String create_data = null;
    private static String update_data = null;
    private static String entityType_Id = null;

    @BeforeTest
    public void userLogin() {
        loginToElevate(PropertyLoader.PROP_LIST.getProperty("elevate.login.contentcreator"), PropertyLoader.PROP_LIST.getProperty("elevate.login.contentcreator.password"));
    }

    @Test(description = "Test case to create entity using valid CSV")
    public void testValidCsvCreate() {
        create_data = entityTypeName;
        modifyCsvFile("src/main/resources/bulk_entity_type_create.csv", create_data, 1, 0);
        Response response = testUploadCsvFile("src/main/resources/bulk_entity_type_create.csv", "elevate.qa.bulkenitytype.create.endpoint");
        response.prettyPrint();
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertTrue(response.asString().contains("SUCCESS"));
        logger.info("Validations related to valid CSV bulk upload entity file is verified");
    }

    @Test(description = "Test case to create entity using Invalid CSV")
    //This negative scenario cannot be verified at the moment because of the below issue
    //https://katha.shikshalokam.org/bug-view-1971.html
    public void testInValidCsvCreate() {
        Response response = testUploadCsvFile("src/main/resources/bulk_entity__type_invalid_ElevateProject.csv", "elevate.qa.bulkenitytype.create.endpoint");
        response.prettyPrint();
        Assert.assertEquals(response.getStatusCode(), 400);
        //Assertion statement to be added once the issue is fixed
        logger.info("Validations related to Invalid CSV bulk upload entity file is verified");
    }

    @Test(description = "Test case to update the entity using valid CSV")
    public void testValidCsvUpdate() {
        Response response = TestElevateEntityTypeCRUDOperations.fetchEntityType(entityTypeName);
        response.prettyPrint();
        List<String> getEntitytypeName = response.jsonPath().getList("result.name");
        List<String> getEntitytypeID = response.jsonPath().getList("result._id");
        int i = 0;
        if (getEntitytypeName.contains(entityTypeName)) {
            while (i < getEntitytypeName.size()) {
                entityTypeName = getEntitytypeName.get(i);
                entityType_Id = getEntitytypeID.get(i);
                logger.info("Entity with name : " + entityTypeName + " found. _id: " + entityType_Id);
                break;
            }
        } else {
            fail(response.prettyPrint() + "Entity type not found");
        }
        Assert.assertTrue(getEntitytypeName.contains(entityTypeName), "entity fetched successfully");
        update_data = entityTypeName + "xyz";
        modifyCsvFile("src/main/resources/bulk_entity_type_update_operations.csv", update_data, 1, 0);
        update_data = entityType_Id;
        modifyCsvFile("src/main/resources/bulk_entity_type_update_operations.csv", update_data, 1, 4);
        Response responseUpload = testUploadCsvFile("src/main/resources/bulk_entity_type_update_operations.csv", "elevate.qa.bulkenitytype.create.endpoint");
        responseUpload.prettyPrint();
        Assert.assertEquals(responseUpload.getStatusCode(), 200);
        Assert.assertTrue(responseUpload.asString().contains("SUCCESS"));
        logger.info("Validations related to valid CSV bulk update entity file is verified");
    }

    @Test(description = "Test case to update the entity using Invalid CSV")
    //This negative scenario cannot be verified at the moment because of the below issue
    //https://katha.shikshalokam.org/bug-view-1971.html
    public void testInValidCsvUpdate() {
        Response response = testUploadCsvFile("src/main/resources/bulk_entity__type_invalid_ElevateProject.csv", "elevate.qa.bulkenitytype.update.endpoint");
        Assert.assertEquals(response.getStatusCode(), 400);
        logger.info("Validations related to Invalid CSV bulk update entity file is verified");
    }

    //Method to upload CSV file path and endpoint is dynamic so that it can be called and re-used in future
    public static Response testUploadCsvFile(String path, String endpoint) {
        // Path to the CSV file
        File csvFile = new File(path);
        if (!csvFile.exists()) {
            logger.error("CSV file does not exist: " + csvFile.getAbsolutePath());
        }
        // Send the POST request to upload the CSV file
        Response response = given().multiPart("entityTypes", csvFile)
                .header("Authorization", INTERNAL_ACCESS_TOKEN)  // Authentication token
                .header("internal-access-token", INTERNAL_ACCESS_TOKEN)  // Internal access token header
                .header("x-authenticated-token", X_AUTH_TOKEN)  // x-authenticated-token header
                .header("Content-Type", "multipart/form-data")  // Content-Type for file upload
                .post(BASE_URL + PropertyLoader.PROP_LIST.getProperty(endpoint));
        logger.info("Response Status Code: " + response.getStatusCode());
        if (response.getStatusCode() != 200) {
            fail(response.prettyPrint() + "ERROR...!!!!!");
        }
        return response;
    }

    public static File modifyCsvFile(String filePath, String edit_data, int row, int column) {
        // Read the CSV file
        List<String[]> csvData;
        try {
            csvData = Files.readAllLines(Paths.get(filePath)).stream()
                    .map(line -> line.split(",", -1))  // -1 keeps empty values
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error reading CSV file: " + e.getMessage(), e);
        }
        // modifying the specific data using row and column location in the CSV
        if (row < csvData.size() && column < csvData.get(row).length) {
            csvData.get(row)[column] = edit_data.trim(); // Modify the specific value and trim it
        } else {
            throw new RuntimeException("Row or column index out of bounds.");
        }
        // Write all rows back to the CSV file
        try (FileWriter fileWriter = new FileWriter(new File(filePath));
             CSVWriter writer = new CSVWriter(fileWriter,
                     CSVWriter.DEFAULT_SEPARATOR, // Comma as separator
                     CSVWriter.NO_QUOTE_CHARACTER, // No extra quoting unless necessary
                     CSVWriter.DEFAULT_ESCAPE_CHARACTER, // Default escape
                     CSVWriter.DEFAULT_LINE_END)) { // Default line ending
            writer.writeAll(csvData); // Write back all modified rows
        } catch (Exception e) {
            throw new RuntimeException("Error writing to CSV file: " + e.getMessage(), e);
        }
        return new File(filePath);
    }
}





