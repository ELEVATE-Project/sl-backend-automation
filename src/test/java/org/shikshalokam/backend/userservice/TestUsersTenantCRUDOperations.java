package org.shikshalokam.backend.userservice;

import static io.restassured.RestAssured.given;
import static org.shikshalokam.backend.PropertyLoader.PROP_LIST;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.shikshalokam.backend.userServiceUtility.CommonUtilityUserService;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class TestUsersTenantCRUDOperations extends UserServiceBaseTest {

    private static final Logger logger =
            LogManager.getLogger(TestUsersTenantCRUDOperations.class);

    private String adminToken;

    // Random tenant values
    private String tenantCode;
    private String tenantName;
    private String tenantDescription;
    private String tenantDomain;
    private String tenantLogo;
    private String addedDomain;

    @BeforeClass
    public void init() {

        logger.info("Generating Admin Token");

        adminToken =
                CommonUtilityUserService.generateAdminToken();

        if (adminToken == null || adminToken.isEmpty()) {

            throw new RuntimeException(
                    "Admin token generation failed"
            );
        }

        // Generate random tenant data

        String random =
                UUID.randomUUID()
                        .toString()
                        .replace("-", "")
                        .substring(0, 6);

        tenantCode = "autotenant" + random;
        tenantName = "tenant_" + random;
        tenantDescription = "description_" + random;
        tenantDomain = "tenant" + random + ".sl.com";

        tenantLogo =
                PROP_LIST.getProperty(
                        "userservice.tenant.logo"
                );

        logger.info("Admin Token Generated Successfully");
    }

    @Test(priority = 1)
    public void testCreateTenant()
            throws URISyntaxException {

        logger.info("Started Create Tenant API");

        Response response = given()
                .header("X-auth-token", adminToken)
                .contentType(ContentType.JSON)
                .body(getTenantRequestBody())
                .when()
                .post(getEndpoint(
                        "userservice.create.tenant.endpoint"
                ));

        printResponse(
                "Create Tenant",
                response
        );

        String responseBody =
                response.getBody().asString();

        assertEquals(
                response.getStatusCode(),
                202,
                "Create Tenant API failed"
        );

        assertTrue(
                responseBody.contains(tenantName)
        );

        logger.info(
                "Tenant Created Successfully"
        );
    }

    @Test(priority = 2)
    public void testUpdateTenant()
            throws URISyntaxException {

        logger.info("Started Update Tenant API");

        tenantName =
                tenantName + "_updated";

        tenantDescription =
                tenantDescription + "_updated";

        URI updateEndpoint = new URI(
                PROP_LIST.getProperty(
                        "userservice.create.tenant.endpoint"
                )
                        + "/"
                        + tenantCode
        );

        Response response = given()
                .header("X-auth-token", adminToken)
                .contentType(ContentType.JSON)
                .body(getTenantRequestBody())
                .when()
                .post(updateEndpoint);

        printResponse(
                "Update Tenant",
                response
        );

        String responseBody =
                response.getBody().asString();

        assertEquals(
                response.getStatusCode(),
                202,
                "Update Tenant API failed"
        );

        assertTrue(
                responseBody.contains(
                        "Tenant updated successfully."
                )
        );

        assertTrue(
                responseBody.contains(
                        tenantName
                )
        );

        logger.info(
                "Tenant Updated Successfully"
        );
    }

    @Test(priority = 3)
    public void testAddDomainToTenant()
            throws URISyntaxException {

        logger.info("Started Add Domain API");

        addedDomain =
                "test"
                        + UUID.randomUUID()
                        .toString()
                        .replace("-", "")
                        .substring(0, 5)
                        + ".domain.com";

        URI addDomainEndpoint = new URI(
                PROP_LIST.getProperty(
                        "userservice.add.domain.endpoint"
                )
                        + "/"
                        + tenantCode
        );

        HashMap<String, Object> requestBody =
                new HashMap<>();

        ArrayList<String> domains =
                new ArrayList<>();

        domains.add(addedDomain);

        requestBody.put(
                "domains",
                domains
        );

        Response response = given()
                .header("X-auth-token", adminToken)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(addDomainEndpoint);

        printResponse(
                "Add Domain",
                response
        );

        String responseBody =
                response.getBody().asString();

        assertEquals(
                response.getStatusCode(),
                202,
                "Add Domain API failed"
        );

        assertTrue(
                responseBody.contains(
                        addedDomain
                )
        );

        logger.info(
                "Domain Added Successfully"
        );
    }

    @Test(priority = 4)
    public void testRemoveDomainFromTenant()
            throws URISyntaxException {

        logger.info("Started Remove Domain API");

        URI removeDomainEndpoint = new URI(
                PROP_LIST.getProperty(
                        "userservice.remove.domain.endpoint"
                )
                        + "/"
                        + tenantCode
        );

        HashMap<String, Object> requestBody =
                new HashMap<>();

        ArrayList<String> domains =
                new ArrayList<>();

        domains.add(addedDomain);

        requestBody.put(
                "domains",
                domains
        );

        Response response = given()
                .header("X-auth-token", adminToken)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(removeDomainEndpoint);

        printResponse(
                "Remove Domain",
                response
        );

        String responseBody =
                response.getBody().asString();

        assertEquals(
                response.getStatusCode(),
                202,
                "Remove Domain API failed"
        );

        assertTrue(
                responseBody.contains(
                        addedDomain
                ),
                "Removed domain not found in response"
        );

        logger.info(
                "Domain Removed Successfully"
        );
    }
    @Test(priority = 5)
    public void testReadTenant()
            throws URISyntaxException {

        logger.info("Started Read Tenant API");

        URI readTenantEndpoint = new URI(
                PROP_LIST.getProperty(
                        "userservice.read.tenant.endpoint"
                )
                        + "/"
                        + tenantCode
        );

        Response response = given()
                .header("X-auth-token", adminToken)
                .contentType(ContentType.JSON)
                .when()
                .get(readTenantEndpoint);

        printResponse(
                "Read Tenant",
                response
        );

        String responseBody =
                response.getBody().asString();

        assertEquals(
                response.getStatusCode(),
                202,
                "Read Tenant API failed"
        );

        assertTrue(
                responseBody.contains(tenantCode)
        );

        assertTrue(
                responseBody.contains(tenantName)
        );

        assertTrue(
                responseBody.contains(
                        tenantDescription
                )
        );

        assertTrue(
                responseBody.contains(
                        tenantDomain
                )
        );

        assertFalse(
                responseBody.contains(
                        addedDomain
                ),
                "Removed domain is still present"
        );

        logger.info(
                "Tenant Read Successfully"
        );
    }
    @Test(priority = 6)
    public void testListTenants()
            throws URISyntaxException {

        logger.info("Started Tenant List API");

        Response response = given()
                .header("X-auth-token", adminToken)
                .contentType(ContentType.JSON)
                .when()
                .get(getEndpoint(
                        "userservice.list.tenant.endpoint"
                ));

        printResponse(
                "Tenant List",
                response
        );

        String responseBody =
                response.getBody().asString();

        assertEquals(
                response.getStatusCode(),
                202,
                "Tenant List API failed"
        );

        assertTrue(
                responseBody.contains(
                        tenantCode
                ),
                "Created tenant code not found"
        );

        assertTrue(
                responseBody.contains(
                        tenantName
                ),
                "Created tenant name not found"
        );

        logger.info(
                "Tenant List API validated successfully"
        );
    }
    private HashMap<String, Object>
    getTenantRequestBody() {

        HashMap<String, Object> requestBody =
                new HashMap<>();

        requestBody.put(
                "name",
                tenantName
        );

        requestBody.put(
                "description",
                tenantDescription
        );

        requestBody.put(
                "code",
                tenantCode
        );

        requestBody.put(
                "logo",
                tenantLogo
        );

        requestBody.put(
                "status",
                "ACTIVE"
        );

        requestBody.put(
                "theming",
                new HashMap<>()
        );

        HashMap<String, Object> meta =
                new HashMap<>();

        ArrayList<String> factors =
                new ArrayList<>();

        factors.add("professional_role");
        factors.add("professional_subroles");
        factors.add("organizations");

        ArrayList<String> observableEntityKeys =
                new ArrayList<>();

        observableEntityKeys.add(
                "professional_subroles"
        );

        ArrayList<String> optionalFactors =
                new ArrayList<>();

        optionalFactors.add("state");
        optionalFactors.add("district");
        optionalFactors.add("block");
        optionalFactors.add("cluster");
        optionalFactors.add("school");

        meta.put(
                "factors",
                factors
        );

        meta.put(
                "observableEntityKeys",
                observableEntityKeys
        );

        meta.put(
                "optional_factors",
                optionalFactors
        );

        requestBody.put(
                "meta",
                meta
        );

        ArrayList<String> domains =
                new ArrayList<>();

        domains.add(
                tenantDomain
        );

        requestBody.put(
                "domains",
                domains
        );

        logger.info(
                "Request Body : {}",
                requestBody
        );

        return requestBody;
    }
    private URI getEndpoint(String propertyKey)
            throws URISyntaxException {

        return new URI(
                PROP_LIST.getProperty(propertyKey)
        );
    }

    private void printResponse(
            String apiName,
            Response response) {

        response.prettyPrint();

        System.out.println(
                apiName + " Status Code : "
                        + response.getStatusCode()
        );

        System.out.println(
                apiName + " Response Body : "
                        + response.getBody().asString()
        );
    }
}