package org.shikshalokam.backend.userservice;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.shikshalokam.backend.PropertyLoader;
import org.shikshalokam.backend.userServiceUtility.CommonUtilityUserService;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.util.HashMap;

import static io.restassured.RestAssured.given;

public class TestUsersBulkUpload extends UserServiceBaseTest {

    private static final Logger logger =
            LogManager.getLogger(TestUsersBulkUpload.class);

    private String adminToken;

    @BeforeMethod
    public void setup() {

        RestAssured.baseURI =
                PropertyLoader.PROP_LIST.getProperty(
                        "userservice.qa.api.base.url"
                );

        logger.info("Generating Admin Token");

        adminToken =
                CommonUtilityUserService.generateAdminToken();

        logger.info("Admin Token Generated Successfully");
    }

    @Test(description = "Bulk Upload Users")
    public void testBulkUserUpload() throws Exception {

        // STEP 1 : Generate Signed URL
        Response signedUrlResponse =
                given()
                        .header(
                                "X-auth-token",
                                adminToken
                        )
                        .queryParam(
                                "fileName",
                                PropertyLoader.PROP_LIST.getProperty(
                                        "userservice.bulkupload.csv.filename"
                                )
                        )
                        .contentType("text/plain")
                        .body("")
                        .when()
                        .get(
                                PropertyLoader.PROP_LIST.getProperty(
                                        "userservice.bulkupload.getsignedurl.endpoint"
                                )
                        );

        signedUrlResponse.prettyPrint();

        Assert.assertEquals(
                signedUrlResponse.getStatusCode(),
                200,
                "Failed To Generate Signed URL"
        );

        String signedUrl =
                signedUrlResponse.jsonPath()
                        .getString("result.signedUrl");

        String filePath =
                signedUrlResponse.jsonPath()
                        .getString("result.filePath");

        logger.info(
                "Signed URL : {}",
                signedUrl
        );

        logger.info(
                "File Path : {}",
                filePath
        );

        // STEP 2 : Upload CSV To GCP Using Multipart Binary Upload
        File csvFile =
                new File(
                        PropertyLoader.PROP_LIST.getProperty(
                                "userservice.bulkupload.csv.path"
                        )
                );

        HttpPut httpPut =
                new HttpPut(signedUrl);

        HttpEntity entity =
                MultipartEntityBuilder.create()
                        .addBinaryBody(
                                "file",
                                csvFile,
                                ContentType.MULTIPART_FORM_DATA,
                                csvFile.getName()
                        )
                        .build();

        httpPut.setEntity(entity);

        httpPut.setHeader(
                "Content-Type",
                "multipart/form-data"
        );

        CloseableHttpClient httpClient =
                HttpClients.createDefault();

        CloseableHttpResponse uploadResponse =
                httpClient.execute(httpPut);

        int uploadStatusCode =
                uploadResponse.getStatusLine().getStatusCode();

        logger.info(
                "Upload Status Code : {}",
                uploadStatusCode
        );

        Assert.assertTrue(
                uploadStatusCode == 200
                        || uploadStatusCode == 201,
                "CSV Upload Failed"
        );

        uploadResponse.close();

        httpClient.close();

        // STEP 3 : Trigger Bulk User Upload API
        HashMap<String, Object> requestBody =
                new HashMap<>();

        requestBody.put(
                "file_path",
                filePath
        );

        requestBody.put(
                "upload_type",
                "upload"
        );

        logger.info(
                "Bulk Upload Request Body : {}",
                requestBody
        );

        Response bulkUploadResponse =
                given()
                        .header(
                                "X-auth-token",
                                adminToken
                        )
                        .header(
                                "x-tenant-code",
                                PropertyLoader.PROP_LIST.getProperty(
                                        "userservice.bulkupload.tenant.code"
                                )
                        )
                        .header(
                                "x-org-code",
                                PropertyLoader.PROP_LIST.getProperty(
                                        "userservice.bulkupload.org.code"
                                )
                        )
                        .contentType("application/json")
                        .body(requestBody)
                        .when()
                        .post(
                                PropertyLoader.PROP_LIST.getProperty(
                                        "userservice.bulkupload.create.endpoint"
                                )
                        );

        bulkUploadResponse.prettyPrint();

        Assert.assertEquals(
                bulkUploadResponse.getStatusCode(),
                200,
                "Bulk User Upload Failed"
        );

        logger.info(
                "Bulk User Upload Completed Successfully"
        );
    }
}