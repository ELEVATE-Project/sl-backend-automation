package org.shikshalokam.backend;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.shikshalokam.backend.MentorEDBaseTest.X_AUTH_TOKEN;

public class MentorEDSessionUpdate extends MentorBase {
    private static final Logger logger = LogManager.getLogger(MentorEDSessionUpdate.class);
    public static List<String> sessionIds = new ArrayList<>();

    // Method to read JSON file
    public static List<String> readJsonFile(String filePath) throws IOException, org.json.simple.parser.ParseException {
        JSONParser parser = new JSONParser();
        try (FileReader reader = new FileReader(filePath)) {
            JSONArray jsonArray = (JSONArray) parser.parse(reader);
            List<String> sessionIds = new ArrayList<>();
            for (Object obj : jsonArray) {
                JSONObject jsonObject = (JSONObject) obj;
                sessionIds.add(jsonObject.toJSONString());
            }
            return sessionIds;
        }
    }

    // Method to update sessions and return session IDs
    public static List<String> updateSessions() {
        List<String> fetchedSessionIds = new ArrayList<>();
        try {
            RestAssured.baseURI = PropertyLoader.PROP_LIST.get("mentor.qa.api.base.url").toString();
            String jsonFilePath = "src/main/resources/managersession_update_payload.json"; // Update this path accordingly

            List<String> jsonBodies = readJsonFile(jsonFilePath);

            for (String jsonBody : jsonBodies) {
                String sessionUpdateEndPoint = "mentoring/v1/sessions/update";
                Response response = given()
                        .header("X-auth-token", "bearer " + X_AUTH_TOKEN)
                        .contentType("application/json")
                        .body(jsonBody)
                        .post(new URI(sessionUpdateEndPoint));

                if (response.getStatusCode() != 201) {
                    logger.info("Failed to update the session. Status code: " + response.getStatusCode());
                    logger.info(response.asPrettyString());
                } else {
                    logger.info("Session updated successfully.");
                    String sessionId = response.body().jsonPath().get("result.id").toString();
                    fetchedSessionIds.add(sessionId);  // Add each session ID to the list
                    logger.info("Session ID fetched: " + sessionId);
                }
            }
        } catch (IOException | org.json.simple.parser.ParseException e) {
            logger.error("Exception occurred while reading the JSON file: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            logger.error("Exception occurred while updating the sessions: " + e.getMessage());
            e.printStackTrace();
        }
        return fetchedSessionIds;
    }

    // Method to read and update the CSV file using plain Java I/O and String.split()
    public static void updateCSVWithSessionIds(List<String> sessionIds, String csvFilePath) {
        List<String> updatedCSVLines = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String header = br.readLine();  // Read header
            updatedCSVLines.add(header);  // Add header to updated list
            String line;
            int idIndex = -1;

            // Split the header to find the index of the 'id' column
            String[] headers = header.split(",");
            for (int i = 0; i < headers.length; i++) {
                if (headers[i].equalsIgnoreCase("id")) {
                    idIndex = i;
                    break;
                }
            }

            if (idIndex == -1) {
                logger.error("'id' column not found in CSV header.");
                return;
            }

            // Update each line of the CSV with session IDs
            int rowIndex = 0;
            while ((line = br.readLine()) != null && rowIndex < sessionIds.size()) {
                String[] columns = line.split(",");

                // Ensure the row has the correct number of columns before updating
                if (columns.length > idIndex) {
                    columns[idIndex] = sessionIds.get(rowIndex);  // Update the 'id' column with session ID
                } // If the row has fewer columns, just leave it untouched

                updatedCSVLines.add(String.join(",", columns));  // Add the updated line to the list
                rowIndex++;
            }

            // If more session IDs exist than rows, add new lines with only the 'id' column filled
            while (rowIndex < sessionIds.size()) {
                String[] newRow = new String[headers.length];  // Create a new row with same number of columns as header
                newRow[idIndex] = sessionIds.get(rowIndex);  // Set the 'id' column with session ID
                updatedCSVLines.add(String.join(",", newRow));  // Only the 'id' will be filled, other columns left blank
                rowIndex++;
            }

        } catch (IOException e) {
            logger.error("Error reading the CSV file: " + e.getMessage());
            e.printStackTrace();
        }

        // Write the updated CSV back to the file
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(csvFilePath))) {
            for (String updatedLine : updatedCSVLines) {
                bw.write(updatedLine);
                bw.newLine();
            }
            logger.info("CSV file updated successfully with session IDs.");
        } catch (IOException e) {
            logger.error("Error writing the updated CSV file: " + e.getMessage());
            e.printStackTrace();
        }
    }




}
