package org.shikshalokam.backend;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static org.shikshalokam.backend.MentorEDBaseTest.X_AUTH_TOKEN;
import static org.shikshalokam.backend.MentorEDBaseTest.loginToMentorED;

public class MentorEDSessionUpdate extends MentorBase {
    private static final Logger logger = LogManager.getLogger(MentorEDSessionUpdate.class);

    public static List<String> updateSessions() {
        List<String> fetchedSessionIds = new ArrayList<>();
        try {
            RestAssured.baseURI = PropertyLoader.PROP_LIST.get("mentor.qa.api.base.url").toString();
            String jsonFilePath = "src/main/resources/managersession_update_payload.json"; // Update this path accordingly

            // JSONParser for parsing the JSON file
            JSONParser parser = new JSONParser();

            // Reading and parsing the JSON file
            try (FileReader reader = new FileReader(jsonFilePath)) {
                JSONArray jsonArray = (JSONArray) parser.parse(reader);

                // Iterate over JSON objects in the array
                for (Object obj : jsonArray) {
                    JSONObject jsonObject = (JSONObject) obj;
                    String jsonBody = jsonObject.toJSONString(); // Convert JSON object to string for request body

                    // Send the session update request
                    String sessionUpdateEndPoint = "mentoring/v1/sessions/update";
                    Response response = RestAssured.given()
                            .header("X-auth-token", "bearer " + X_AUTH_TOKEN)
                            .contentType("application/json")
                            .body(jsonBody)
                            .post(new URI(sessionUpdateEndPoint));

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
                }
            } catch (IOException | org.json.simple.parser.ParseException e) {
                logger.error("Exception occurred while reading the JSON file: " + e.getMessage());
                e.printStackTrace();
            }

        } catch (Exception e) {
            logger.error("Exception occurred while creating the sessions: " + e.getMessage());
            e.printStackTrace();
        }
        return fetchedSessionIds;
    }
    public static void updateCSVWithSessionIds(List<String> sessionIds, String sourceCsvFilePath, String targetCsvFilePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(sourceCsvFilePath))) {
            // Read the header and store it as the first line
            String header = br.readLine();

            // Collect all the lines after header
            List<String> csvLines = br.lines().collect(Collectors.toList());

            // Replace 'SessionId' placeholder in the existing rows, ensuring the session ID count matches
            List<String> updatedCSVLines = IntStream.range(0, csvLines.size())
                    .mapToObj(i -> csvLines.get(i).contains("SessionId") && i < sessionIds.size()
                            ? csvLines.get(i).replace("SessionId", sessionIds.get(i))
                            : csvLines.get(i))
                    .collect(Collectors.toList());

            // If there are more session IDs than rows, append the remaining session IDs
            updatedCSVLines.addAll(
                    IntStream.range(csvLines.size(), sessionIds.size())
                            .mapToObj(i -> "SessionId," + sessionIds.get(i))
                            .collect(Collectors.toList())
            );
            // Add the header back at the beginning
            updatedCSVLines.add(0, header);

            // Write the updated CSV to the target path
            writeCsvToTarget(updatedCSVLines, targetCsvFilePath);

        } catch (IOException e) {
            logger.error("Error reading the CSV file: " + e.getMessage());
            e.printStackTrace();
        }
    }
    private static void writeCsvToTarget(List<String> updatedCSVLines, String targetCsvFilePath) {
        Path targetFolderPath = Paths.get(targetCsvFilePath).getParent();  // Get the parent folder path

        try {
            if (!Files.exists(targetFolderPath)) {
                Files.createDirectories(targetFolderPath);  // Create the folder if it doesn't exist
            }

            try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(targetCsvFilePath))) {
                // Write all lines at once, joining them with the system's line separator
                bw.write(String.join(System.lineSeparator(), updatedCSVLines));

                logger.info("CSV file updated and saved successfully to " + targetCsvFilePath);
            } catch (IOException e) {
                logger.error("Error writing the updated CSV file: " + e.getMessage());
            }

        } catch (IOException e) {
            logger.error("Error creating target folder: " + e.getMessage());
        }
    }

    public static void sessionCreationAndFetchId() {
        logger.info("Logging into the application:");
        loginToMentorED("jubedhashaik029@gmail.com", "PAssword@@123$");
        // Fetch session IDs after updating sessions
        List<String> fetchedSessionIds = updateSessions();
        // Update CSV files with the fetched session IDs
        updateCSVWithSessionIds(fetchedSessionIds, "src/main/resources/bulk_session_edit.csv", "target/bulksession_files/session_edit.csv");
        updateCSVWithSessionIds(fetchedSessionIds, "src/main/resources/bulk_session_delete.csv", "target/bulksession_files/session_delete.csv");
    }
}
