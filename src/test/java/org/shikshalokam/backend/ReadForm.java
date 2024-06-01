package org.shikshalokam.backend;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

import static io.restassured.RestAssured.given;

public class ReadForm extends MentorEDBaseTest {
    private static final Logger logger = LogManager.getLogger(ReadForm.class);
    private String reaFormEndpoint = "mentoring/v1/form/read";

    // PreCreate the entity needed with name=label=entityType with same name , example "Location" and pass the same as "entityName"
    // if want to add entity pass true , else false to parameter  "addEentity"
    public JSONObject readForm(String orgAdminUser, String password, String formType, String formSubType, String entityName, boolean addEntity) {
        logger.info("Started calling the readForm:");
        URI reaFormEndpointURI = null;
        JSONObject jsonObject = new JSONObject();
        LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
        LinkedHashMap<String, Object> datamap = new LinkedHashMap<String, Object>();
        loginToMentorED(orgAdminUser, password);
        try {
            reaFormEndpointURI = new URI(reaFormEndpoint);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        map.put("type", formType);
        map.put("sub_type", formSubType);
        jsonObject.putAll(map);

        logger.info("RequestBody for the readForm is : " + jsonObject.toJSONString());
        Response responce = given()
                .header("X-auth-token", "bearer " + X_AUTH_TOKEN)
                .contentType(ContentType.JSON)
                .body(jsonObject)
                .when().post(reaFormEndpointURI);

        Object resultData = responce.getBody().jsonPath().getJsonObject("result.data");
        if (resultData instanceof LinkedHashMap)
            datamap = (LinkedHashMap<String, Object>) resultData;


        map.clear();
        map.put("type", formType);
        map.put("sub_type", formSubType);
        map.put("data", datamap);
        jsonObject.clear();

        jsonObject.putAll(map);

        logger.info(jsonObject.toJSONString());

        Map dataObject = (Map) jsonObject.get("data");
        Map fieldsObject = (Map) dataObject.get("fields");
        List controlsArray = (List) fieldsObject.get("controls");

        if (!addEntity) {
            LinkedHashMap<String, Object> mapEntity = new LinkedHashMap<String, Object>();

            Iterator<Object> iterator = controlsArray.iterator();
            while (iterator.hasNext()) {
                mapEntity = (LinkedHashMap<String, Object>) iterator.next();
                if (mapEntity.get("name").equals(entityName)) {
                    controlsArray.remove(mapEntity);
                    break;
                }
            }


        } else {

            controlsArray.add(entityJsonObject(entityName));
        }


        logger.info(jsonObject.toJSONString());

        return jsonObject;
    }

    // PreCreate the entity needed with name=label=entityType with same name , example "Location"
    public JSONObject entityJsonObject(String entityName) {
        String input = "{\n" +
                "                        \"name\": \"{REPLACE}\",\n" +
                "                        \"label\": \"{REPLACE}\",\n" +
                "                        \"class\": \"ion-no-margin\",\n" +
                "                        \"value\": \"\",\n" +
                "                        \"type\": \"chip\",\n" +
                "                        \"position\": \"\",\n" +
                "                        \"disabled\": false,\n" +
                "                        \"errorMessage\": {\n" +
                "                            \"required\": \"Enter Location\"\n" +
                "                        },\n" +
                "                        \"validators\": {\n" +
                "                            \"required\": true\n" +
                "                        },\n" +
                "                        \"options\": [],\n" +
                "                        \"meta\": {\n" +
                "                            \"entityType\": \"{REPLACE}\",\n" +
                "                            \"addNewPopupHeader\": \"Location\",\n" +
                "                            \"addNewPopupSubHeader\": \"Who is this session for?\",\n" +
                "                            \"showSelectAll\": true,\n" +
                "                            \"showAddOption\": {\n" +
                "                                \"showAddButton\": true,\n" +
                "                                \"addChipLabel\": \"\"\n" +
                "                            }\n" +
                "                        },\n" +
                "                        \"multiple\": true\n" +
                "                    }";

        JSONParser parser = new JSONParser();
        JSONObject jsonObject = null;

        try {
            // Parse the JSON string into a JSONObject
            jsonObject = (JSONObject) parser.parse(input);

            // Print the original JSONObject
            System.out.println("Original JSON: " + jsonObject.toString());

            // Replace the value of a specific key
            jsonObject.put("name", entityName);
            jsonObject.put("label", entityName);
            JSONObject meta = (JSONObject) jsonObject.get("meta");
            meta.put("entityType", entityName);


            // Print the modified JSONObject
            System.out.println("Modified JSON: " + jsonObject.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return jsonObject;

    }

}
