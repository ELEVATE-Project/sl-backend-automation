package org.shikshalokam.backend;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class SMsampleCSVBulkPuload extends MentorEDBaseTest {

    private static final Logger logger = LogManager.getLogger(SMsampleCSVBulkPuload.class);
    private static String readSignedUrl;
    public static String[] signedUrl = new String[2];

    public static File sampleFile;
    public static Response signedURLResponse;

    // Determine environment (QA or Production)
    private static boolean isQAEnvironment() {
        String environment = PropertyLoader.PROP_LIST.get("environment").toString();
        return environment.equalsIgnoreCase("QA");
    }

    private static String[] GetSignedUrlDetails() {
        logger.info("Started calling the GetSignedUrlDetails:");
        URI endpoint;
        try {
            endpoint = new URI(readSignedUrl);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        Response response = given().log().all().header("X-auth-token", "bearer " + X_AUTH_TOKEN).when().get(endpoint);
        signedURLResponse = response;
        logger.info("GetSignedUrlDetails :::" + response.getBody().asString());
        if (response.getStatusCode() == 200) {
            signedUrl[0] = response.jsonPath().getString("result.signedUrl");
            signedUrl[1] = response.jsonPath().getString("result.filePath");
        } else {
            logger.info("Getting Signed URL request failed: " + response.getBody().asString());
        }
        return signedUrl;
    }

    public static void uploadSampleFileToCloud() {
        // Set file name and signed URL based on environment
        String fileName;
        if (isQAEnvironment()) {
            fileName = "bulk_session_creation_auto_Qa.csv";
            readSignedUrl = "/mentoring/v1/cloud-services/getSignedUrl?fileName=" + fileName;
        } else {
            fileName = "bulk_session_creation.csv";
            readSignedUrl = "/mentoring/v1/cloud-services/getSignedUrl?fileName=" + fileName;
        }

        signedUrl = GetSignedUrlDetails();
        sampleFile = new File("./src/main/resources/" + fileName);

        String filePath;
        Map<String, String> stringMap = new HashMap<>();

        try {
            filePath = sampleFile.getCanonicalPath();
            logger.info("Sample File Path: " + filePath);
            logger.info("File exists status: " + sampleFile.exists());
            logger.info("Signed URL: " + signedUrl[0]);
            logger.info("File signed URL: " + signedUrl[1]);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String GoogleAccessId = null;
        String Expires = null;
        String Signature = null;
        String baseUrl;

        try {
            String[] urlParts = signedUrl[0].split("\\?");
            baseUrl = urlParts[0];
            String query = urlParts.length > 1 ? urlParts[1] : "";
            stringMap = splitQuery(query);

            if (isQAEnvironment()) {
                // Google Cloud (QA)
                GoogleAccessId = stringMap.get("GoogleAccessId");
                Expires = stringMap.get("Expires");
                Signature = stringMap.get("Signature");
            } else {
                // AWS S3 (Production)
                baseUrl = signedUrl[0]; // Use full URL directly for S3
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        URI signedURI;
        try {
            signedURI = new URI(baseUrl);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        // Perform upload based on environment
        Response response;
        if (isQAEnvironment()) {
            // Google Cloud (QA)
            response = RestAssured
                    .given()
                    .urlEncodingEnabled(false)
                    .log().all()
                    .header("Content-Type", "multipart/form-data")
                    .queryParam("GoogleAccessId", GoogleAccessId)
                    .queryParam("Expires", Expires)
                    .queryParam("Signature", Signature)
                    .body(sampleFile)
                    .when().put(signedURI);
        } else {
            // AWS S3 (Production)
            response = RestAssured
                    .given()
                    .urlEncodingEnabled(false)
                    .log().all()
                    .header("Content-Type", "multipart/form-data")
                    .body(sampleFile)
                    .when().put(signedURI);
        }

        // Check response and log accordingly
        if (response.getStatusCode() == 200) {
            logger.info("File uploaded successfully. Status Code: " + response.getStatusCode());
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("file_path", signedUrl[1]);
            RestAssured.baseURI = PropertyLoader.PROP_LIST.get("mentor.qa.api.base.url").toString();
            response = RestAssured
                    .given()
                    .log().all()
                    .header("X-auth-token", "bearer " + X_AUTH_TOKEN)
                    .header("Content-Type", "application/json")
                    .body(jsonObject)
                    .post("mentoring/v1/org-admin/uploadSampleCSV");

            if (response.getStatusCode() == 200) {
                logger.info("Sample file upload completed: " + response.getBody().asString());
            } else {
                logger.info("Sample file upload failed: " + response.getBody().asString());
            }
        } else {
            logger.info("Failed to upload the file. Status code: " + response.getStatusCode());
            logger.info(response.asPrettyString());
        }
    }

    public static Map<String, String> splitQuery(String query) {
        Map<String, String> queryPairs = new HashMap<>();
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            String key = idx > 0 ? pair.substring(0, idx) : pair;
            String value = idx > 0 && pair.length() > idx + 1 ? pair.substring(idx + 1) : null;
            queryPairs.put(key, value);
        }
        return queryPairs;
    }
}
