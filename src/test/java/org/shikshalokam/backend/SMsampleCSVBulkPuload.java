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
    private static String readSignedUrl = "/mentoring/v1/cloud-services/getSignedUrl?fileName=bulk_session_creation_auto.csv";
    public static String[] signedUrl = new String[2];

    public static File sampleFile;
    public static Response signedURLResponse;

    private static String[] GetSignedUrlDetails() {

        logger.info("Started calling the GetSignedUrlDetails:");
        URI endpoint;
        try {
            endpoint = new URI(readSignedUrl);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        Response responce = given().log().all().header("X-auth-token", "bearer " + X_AUTH_TOKEN).when().get(endpoint);
        signedURLResponse = responce;
        logger.info("GetSignedUrlDetails    :::" + responce.getBody().asString());
        if (responce.getStatusCode() == 200) {

            signedUrl[0] = responce.jsonPath().getString("result.signedUrl");
            signedUrl[1] = responce.jsonPath().getString("result.filePath");


        } else {

            logger.info("Getting Signed url requestfailed  :" + responce.getBody().asString());
        }


        return signedUrl;
    }


    public static void uploadSampleFileToCloud() {

        signedUrl = GetSignedUrlDetails();
        sampleFile = new File("./src/main/resources/bulk_session_creation_auto.csv");
        URI signedURI = null;
        String filePath = null;
        Map<String, String> stringMap = new HashMap<String, String>();

        String GoogleAccessId = null;
        String Expires = null;
        String Signature = null;
        String baseUrl = null;

        try {
            filePath = sampleFile.getCanonicalPath();
            logger.info("Sample File Path :" + filePath);
            logger.info("File exists status : " + sampleFile.exists());
            logger.info("singnedUrl :" + signedUrl[0]);
            logger.info("File singnedUrl :" + signedUrl[1]);


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {

            String[] urlParts = signedUrl[0].split("\\?");
            baseUrl = urlParts[0];
            String query = urlParts.length > 1 ? urlParts[1] : "";
            stringMap = splitQuery(query);

            signedURI = new URI(baseUrl);
            logger.info("singnedUrl as string  :" + signedURI.toString());


            GoogleAccessId = stringMap.get("GoogleAccessId");
            Expires = stringMap.get("Expires");
            Signature = stringMap.get("Signature");

            System.out.println(GoogleAccessId);
            System.out.println(Expires);
            System.out.println(Signature);

        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        RestAssured.baseURI = "";
        Response responce = null;
        responce = RestAssured
                .given()
                .urlEncodingEnabled(false)
                .log().all()
                .header("Content-Type", "multipart/form-data")
                .header("Accept-Encoding", "gzip, deflate, br")
                .header("Accept-Encoding", "gzip, deflate, br")
                .queryParam("GoogleAccessId", GoogleAccessId)
                .queryParam("Expires", Expires)
                .queryParam("Signature", Signature)
                .body(sampleFile)
                .when().put(signedURI);


        if (responce.getStatusCode() == 200) {

            logger.info(" uploaded the file to Cloud   :" + responce.getStatusCode());
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("file_path", signedUrl[1]);
            RestAssured.baseURI = PropertyLoader.PROP_LIST.get("mentor.qa.api.base.url").toString();
            responce = null;
            responce = RestAssured
                    .given()
                    .log().all()
                    .header("X-auth-token", "bearer " + X_AUTH_TOKEN)
                    .header("Content-Type","application/json")
                    .when()
                    .body(jsonObject)
                    .post("mentoring/v1/org-admin/uploadSampleCSV");

            if (responce.getStatusCode() == 200) {

                logger.info(" Uploading of sample file completed    :" + responce.getBody().asString());
            } else {

                logger.info(" Uploading of sample file failed    :" + responce.getBody().asString());
            }


        } else {
            System.out.println("After the call  : " + signedUrl[0]);
            logger.info("Unable to upload the file to Cloud   :" + responce.getBody().asString());
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
