package org.shikshalokam.uiPageObjects.ep;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.shikshalokam.backend.PropertyLoader;
import org.shikshalokam.uiPageObjects.PWBasePage;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Pattern;
//Tan90 migration script
public class AppSubmittingResponse extends PWBasePage {
    //    need to implement this class as per the requirement of submitting response by fetching the username and password from the excl sheet,
    private static final Logger logger = LogManager.getLogger(AppSubmittingResponse.class);
    AppSubmittingResponse response;

    public AppSubmittingResponse(String givenTitleName) {
        super(givenTitleName);
        this.response = this;
    }

    public AppSubmittingResponse readDataFromSheet() {
        logger.info("Submitting response started");
        // Updated path as per your project structure
        String filePath = "./src/main/resources/config/Tan90-users-list.xls";

        try (FileInputStream fis = new FileInputStream(new File(filePath));
             Workbook workbook = WorkbookFactory.create(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            DataFormatter formatter = new DataFormatter();

            // Start loop from i = 1 to skip the first row (index 0)
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);

                // Skip if the row is physically empty
                if (row == null) break;

                // Get Column 1 (Index 0)
                Cell cell1 = row.getCell(0);
                String email = formatter.formatCellValue(cell1);

                // Stop if the first cell of the row is empty
                if (email == null || email.trim().isEmpty()) {
                    break;
                }

                // Get Column 2 (Index 1)
                Cell cell2 = row.getCell(1);
                String pass = formatter.formatCellValue(cell2);

                // Output the data
                System.out.println("Col 1: " + email + " | Col 2: " + pass);
                logIntoportal(email, pass);
                provideResponse();
            }

        } catch (Exception e) {
            System.err.println("Could not read file at: " + filePath);
            e.printStackTrace();
        }
        logger.info("Submitting response ended");
        return response;
    }

    public void logIntoportal(String mail, String pass){
        PWBasePage.reInitializePage();
        PWBasePage.page.navigate(PropertyLoader.PROP_LIST.getProperty("ep.url"));
        page.getByLabel("Email / Mobile / Username").click();
        page.getByLabel("Email / Mobile / Username").fill(mail);
        page.getByLabel("Password").click();
        page.getByLabel("Password").fill(pass);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Login")).click();
        page.navigate("https://qa.elevate-shikshalokam.shikshalokam.org/home");
    }

    public void provideResponse(){

        page.locator("div").filter(new Locator.FilterOptions().setHasText(Pattern.compile("^Programs$"))).first().click();
        page.getByText("Tan90 STEM Education").click();
//      1st project // Flow of Heat through a Metal Strip
        projectWithFiveTasks("Flow of Heat through a Metal Strip");
//      2nd project  Testing Acids & Bases
        projectWithFiveTasks("Testing Acids & Bases");
//      3rd project // Neutralisation Reaction
        projectWithFiveTasks("Neutralisation Reaction");
//      4th project //Crystallization
        projectWithFiveTasks("Crystallization");
//      5th project //Human Lungs
        projectWithFiveTasks("Human Lungs");
//      6th project //Preparation of Acid-Base Indicators
        projectWithFiveTasks("Preparation of Acid-Base Indicators");
//      7th project //Human Kidney
        projectWithFiveTasks("Human Kidney");
//      8th project // Symbols of Electrical
        projectWithFiveTasks("Symbols of Electrical Components");
//      9th project //Household Circuits
        projectWithFiveTasks("Household Circuits");
//10th project // Table Fan
        projectWithFiveTasks("Table Fan");
//11th project // Electromagnet
        projectWithFiveTasks("Electromagnet");
//12th project // Light Travels in straight Line
        projectWithFiveTasks("Light Travels in straight Line");
//13th project // Magical Mirror
        projectWithFiveTasks("Magical Mirror");
//14th project // Newton's Wheels
        projectWithFiveTasks("Newton's Wheels");
//15th project // Solar Bulb
        projectWithFiveTasks("Solar Bulb");
//16th project //Phases of the Moon
        projectWithFiveTasks("Phases of the Moon");
//17th project //Automatic Water Dispenser
        projectWithFiveTasks("Automatic Water Dispenser");
//18th project //
        sevenTaskSubmission("Endline Assessment Handbook-2");
//19th project //
        sevenTaskSubmission("Baseline Assessment Handbook 2");
//20th project //Balloon Car
        projectWithFiveTasks("Balloon Car");
//21 project //Image Formation by Concave & Convex
        projectWithFiveTasks("Image Formation by Concave & Convex");
//22nd project //Series and Parallel Circuit
        projectWithFiveTasks("Series and Parallel Circuit");

//        Observation
        page.locator("ion-card-content").filter(new Locator.FilterOptions().setHasText("observations")).click();
        boolean getObsName = page.getByText("Leaner Observation Tool-").isVisible();

        if (getObsName)
        {
            page.getByText("Leaner Observation Tool-").click();
            observationWithOutRubric();
        }
        else {
            observationWithRubric();
        }
    }


    public void startProjectSubmission(){
        page.locator("ion-title").filter(new Locator.FilterOptions().setHasText("projects")).locator("div").click();
        page.getByText("Flow of Heat through a Metal").click();

    }

    public void markCompleted(){
        page.getByText("Not Started").click();
        page.getByText("Completed").click();
        page.getByRole(AriaRole.BANNER).locator("svg").click();
    }
    public void attachEvidenceForTask(){
        page.getByText("Not Started").click();
        page.getByText("Completed").click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Add files")).click();
        page.getByLabel("", new Page.GetByLabelOptions().setExact(true)).check();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Upload").setExact(true)).click();

        Path png = evidencePath("PNG_Evidence.png");
        uploadTaskLevelEvidence(png, "Images");
        page.locator("mat-card").filter(new Locator.FilterOptions().setHasText("Links")).click();
        page.getByPlaceholder("Add link here").click();
        page.getByPlaceholder("Add link here").fill(fetchProperty("ep.url"));
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Save")).click();

        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Attach files")).click();
        page.getByRole(AriaRole.BANNER).locator("svg").click();
    }
    public void clickOnSubmitButton(){
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Submit Improvement")).click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Submit Improvement")).click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Submit")).click();
        page.getByRole(AriaRole.BANNER).locator("svg").click();
    }

    public void projectWithFiveTasks(String ProjectName){
//        startProjectSubmission();
        page.locator("ion-title").filter(new Locator.FilterOptions().setHasText("projects")).locator("div").click();

        page.getByText(ProjectName, new Page.GetByTextOptions().setExact(true)).first().click();
        boolean strt = page.locator("//span[contains(text(), 'Submit Improvement ')]").isVisible();
        if(strt==false){
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Start Improvement")).click();
        }
        page.getByText("Task details").click();
        page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Review the Activity Manual")).click();
        markCompleted();
//        page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Watch the Activity Video")).click();
//        attachEvidenceForTask();
//        page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Conduct the Activity with")).click();
//        markCompleted();
//        page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Conduct the Activity Assessment")).click();
//        markCompleted();
//        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Load more")).click();
//        page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Log into the scanner app and")).click();
//        markCompleted();
//        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Load more")).click();
//        boolean seventh = page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Discuss student results with")).isVisible();
//        if(seventh){
//            page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Discuss student results with")).click();
//            markCompleted();
//        }
//
//        clickOnSubmitButton();
        page.locator("lib-icon-list mat-icon").nth(3).click();
        page.getByRole(AriaRole.BANNER).locator("svg").click();


    }

    public void sevenTaskSubmission(String ProjectName){
        page.locator("ion-title").filter(new Locator.FilterOptions().setHasText("projects")).locator("div").click();
        page.getByText(ProjectName, new Page.GetByTextOptions().setExact(true)).first().click();
        boolean strt = page.locator("//span[contains(text(), 'Submit Improvement ')]").isVisible();
        if(strt==false){
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Start Improvement")).click();
        }
        page.getByText("Task details").click();
        page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Submit student data to the")).click();
        markCompleted();
//        page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Keep ready 1 OMR sheet and 1")).click();
//        attachEvidenceForTask();
//        page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("With examples show students")).click();
//        markCompleted();
//        page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Check that students are using")).click();
//        markCompleted();
//        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Load more")).click();
//        page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Distribute the papers and the")).click();
//        markCompleted();
//        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Load more")).click();
//        page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Log into the scanner app and")).click();
//        markCompleted();
//        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Load more")).click();
//        page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Discuss student results with")).click();
//        markCompleted();
//        clickOnSubmitButton();
        page.locator("lib-icon-list mat-icon").nth(3).click();
        page.getByRole(AriaRole.BANNER).locator("svg").click();
    }

    public void observationWithOutRubric(){
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Add Handbook1_Activity")).click();
        page.getByText("Baseline Assessment").click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Add")).click();
        page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Baseline Assessment")).click();
        page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Observation")).click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Start")).click();
        page.getByLabel("Flow of Heat through a Metal").check();
        page.getByLabel("Very Easy").check();
//        page.getByLabel("Activity Manuals").check();
//        page.getByLabel("Very confident").check();
//        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("2")).click();
//        page.getByLabel("More videos").check();
//        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Submit")).click();
//        page.locator("(//button[@class=\"btn btnMargin\"])[1]").click();
//        page.locator("//button[contains(@class,'back-button mdc-icon-button mat-mdc-icon-button mat-unthemed mat-mdc-button-base')]").click();
    }

    public void observationWithRubric(){
        page.getByText("Facilitator Observation Tool").click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Add Handbook1_Activity")).click();
        page.getByText("Baseline Assessment").click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Add")).click();
        page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Baseline Assessment")).click();
        page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Observation")).click();
        page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Domain 1")).click();
        page.getByText("Observation_HandBook01_Activities").click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Start")).click();
//        page.locator("//input[@id=\"mat-input-0\"]").click();
//        page.locator("//input[@id=\"mat-input-0\"]").fill("user name");
//        page.locator("//input[@id=\"mat-input-1\"]").click();
//        page.locator("//input[@id=\"mat-input-1\"]").fill("2345");
        page.getByLabel("Simple Circuit").check();
//        page.getByLabel("Redirects the question and").check();
//        page.getByLabel("Contains major errors or").check();
//        page.getByLabel("Demonstrates correct use,").check();
//        page.getByLabel("Clearly states the learning").check();
//        page.getByLabel("Moves between groups,").check();
//        page.getByLabel("Gives unrelated homework").check();
//        page.getByLabel("Summarises key points and").check();
//        page.getByLabel("Encourages exploration and").check();
//        page.getByLabel("Changes activity due to").check();
//        page.getByLabel("Quickly adapts and finds an").check();
//        page.getByLabel("Neutral but supportive").check();
//        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Submit")).click();
//        page.locator("(//button[@class=\"btn btnMargin ng-star-inserted\"])[1]").click();
//        page.locator("//button[contains(@class,'back-button mdc-icon-button mat-mdc-icon-button mat-unthemed mat-mdc-button-base')]").click();

    }
}
