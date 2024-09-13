package org.shikshalokam.backend;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static org.shikshalokam.backend.MentorEDBaseTest.X_AUTH_TOKEN;
import static org.shikshalokam.backend.MentorEDBaseTest.loginToMentorED;

public class MentorEDSessionUpdate extends MentorBase {
    private static final Logger logger = LogManager.getLogger(MentorEDSessionUpdate.class);

    public static List<String> updateSessions(String jsonFilePath) {
        List<String> fetchedSessionIds = new ArrayList<>();
        try {
            RestAssured.baseURI = PropertyLoader.PROP_LIST.get("mentor.qa.api.base.url").toString();
            // Parse the JSON file into a JSONObject
            JSONObject jsonObject = (JSONObject) new JSONParser().parse(new FileReader(jsonFilePath));
            // Define the endpoint
            String sessionUpdateEndPoint = "mentoring/v1/sessions/update";
            // Create a single request
            Response response = RestAssured.given()
                    .header("X-auth-token", "bearer " + X_AUTH_TOKEN)
                    .contentType("application/json")
                    .body(jsonObject)  // Directly passing the JSONObject as body
                    .post(URI.create(sessionUpdateEndPoint));

            // Check the response status code
            if (response.getStatusCode() != 201) {
                logger.info("Failed to create the session. Status code: " + response.getStatusCode());
                logger.info(response.asPrettyString());
            } else {
                logger.info("Session created successfully.");
                // Extract the session ID from the response
                String sessionId = response.body().jsonPath().getString("result.id");
                fetchedSessionIds.add(sessionId);  // Add session ID to the list
                logger.info("Session ID fetched: " + sessionId);
            }

        } catch (Exception e) {
            logger.error("Exception occurred while creating the sessions: " + e.getMessage());
            e.printStackTrace();
        }
        return fetchedSessionIds;
    }



    public static void updateCSVWithSessionIds(List<String> sessionIds, String sourceCsvFilePath, String targetCsvFilePath) {
        try {
            // Read the entire CSV content into a string
            String content = new String(Files.readAllBytes(Paths.get(sourceCsvFilePath)));

            // Replace 'SessionId' placeholders with session IDs
            for (String sessionId : sessionIds) {
                content = content.replaceFirst("SessionId", sessionId);
            }
            // Write the updated content to the target file
            Files.write(Paths.get(targetCsvFilePath), content.getBytes());

        } catch (IOException e) {
            logger.error("Error processing the CSV file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void sessionCreationAndFetchId() {
        logger.info("Logging into the application:");
        loginToMentorED("jubedhashaik029@gmail.com", "PAssword@@123$");

        // Define paths to your JSON files
        String[] jsonFilePaths = {"src/main/resources/managersession_update_payload1.json", "src/main/resources/managersession_update_payload2.json"
        };
        // Fetch session IDs from multiple JSON files and combine them
        List<String> allFetchedSessionIds = new ArrayList<>();
        for (String jsonFilePath : jsonFilePaths) {
            allFetchedSessionIds.addAll(updateSessions(jsonFilePath));
        }
        // Update CSV files with the combined session IDs
        updateCSVWithSessionIds(allFetchedSessionIds, "src/main/resources/bulk_session_edit.csv", "target/bulksession_files/session_edit.csv");
        updateCSVWithSessionIds(allFetchedSessionIds, "src/main/resources/bulk_session_delete.csv", "target/bulksession_files/session_delete.csv");
    }
}
