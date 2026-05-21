package org.shikshalokam.uiPageObjects;

import com.microsoft.playwright.*;
import com.microsoft.playwright.assertions.LocatorAssertions;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.shikshalokam.backend.MentorEDBaseTest;
import org.shikshalokam.backend.PropertyLoader;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.testng.Assert.assertTrue;


public class PWBasePage extends MentorEDBaseTest {
    private static final Logger logger = LogManager.getLogger(PWBasePage.class);
    public static BrowserContext browserContext;
    public static Page page;
    public static PWBrowser PWBrowser;
    public static Playwright playwright = Playwright.create();
    public static Browser browser;
    private String pwTitle;

    private static String browserType = PropertyLoader.PROP_LIST.getProperty("mentor.qa.browser");


    public static Boolean headless;
    public static Boolean recordVideo;

    static {
        // Set default values
        headless = true;    // Default is headless mode
        recordVideo = false; // Default is no video recording

        // Check the properties file for headless mode
        if (PropertyLoader.PROP_LIST.getProperty("mentor.qa.browser.setHeadless").equals("false")) {
            headless = false;
        }
        // Check the properties file for video recording
        if (PropertyLoader.PROP_LIST.getProperty("mentor.qa.browser.recordVideo").equals("true")) {
            recordVideo = true;
        }
        initializeBrowser(browserType);
    }


    public static void initializeBrowser(String browserType) {
        switch (browserType) {
            case "chromium":
                logger.info("Using Chromium browser for Test suite");
                PWBasePage.PWBrowser = PWBasePage.PWBrowser.chromium;
                BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions().setHeadless(headless).setSlowMo(2000);

                // Set slowMo if recording is enabled
                if (recordVideo) {
                    launchOptions.setSlowMo(1500); // Adjust the delay as needed
                }
                // Launch the browser
                browser = playwright.chromium().launch(launchOptions);
                // Define context options with viewport size
                Browser.NewContextOptions contextOptions = new Browser.NewContextOptions().setViewportSize(null);
                // Set video recording directory if recording is enabled
                if (recordVideo) {
                    contextOptions.setRecordVideoDir(Paths.get("target", "videos"));
                }
                browserContext = browser.newContext(contextOptions);
                page = browserContext.newPage();
                break;
            case "msedge":
                logger.info("Using Edge browser for Test suite ");
                PWBasePage.PWBrowser = PWBrowser.msedge;
                browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setChannel("msedge").setHeadless(headless).setSlowMo(1500));
                browserContext = browser.newContext();
                page = browserContext.newPage();
                break;
            case "firefox":
                logger.info("Using Firefox browser for Test suite");
                PWBasePage.PWBrowser = PWBrowser.firefox;
                browser = playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(headless).setSlowMo(1500));
                browserContext = browser.newContext();
                page = browserContext.newPage();
                break;
            case "webkit":
                logger.info("Using Safari browser for Test suite");
                PWBasePage.PWBrowser = PWBrowser.webkit;
                browser = playwright.webkit().launch(new BrowserType.LaunchOptions().setHeadless(headless).setSlowMo(1500));
                browserContext = browser.newContext();
                page = browserContext.newPage();
                break;
            case "chromePixel4a":
                logger.info("Using Chrome browser for Test suite with Pixel 4a emulation");
                PWBasePage.PWBrowser = PWBrowser.chromePixel4a;
                browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(headless).setSlowMo(1500));
                browserContext = browser.newContext(new Browser.NewContextOptions()
                        .setUserAgent(PropertyLoader.PROP_LIST.getProperty("mentor.qa.emulation.userAgent"))
                        .setViewportSize(Integer.parseInt(PropertyLoader.PROP_LIST.getProperty("mentor.qa.emulation.viewport.width")),
                                Integer.parseInt(PropertyLoader.PROP_LIST.getProperty("mentor.qa.emulation.viewport.height")))
                        .setDeviceScaleFactor(Double.parseDouble(PropertyLoader.PROP_LIST.getProperty("mentor.qa.emulation.deviceScaleFactor")))
                        .setIsMobile(true)
                        .setHasTouch(true));
                page = browserContext.newPage();
                break;
            case "msedgePixel4a":
                logger.info("Using Edge browser for Test suite with Pixel 4a emulation");
                PWBasePage.PWBrowser = PWBrowser.msedgePixel4a;
                browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setChannel("msedge").setHeadless(headless).setSlowMo(1500));
                browserContext = browser.newContext(new Browser.NewContextOptions()
                        .setUserAgent(PropertyLoader.PROP_LIST.getProperty("mentor.qa.emulation.userAgent"))
                        .setViewportSize(Integer.parseInt(PropertyLoader.PROP_LIST.getProperty("mentor.qa.emulation.viewport.width")),
                                Integer.parseInt(PropertyLoader.PROP_LIST.getProperty("mentor.qa.emulation.viewport.height")))
                        .setDeviceScaleFactor(Double.parseDouble(PropertyLoader.PROP_LIST.getProperty("mentor.qa.emulation.deviceScaleFactor")))
                        .setIsMobile(true)
                        .setHasTouch(true));
                page = browserContext.newPage();
                break;
            // Add cases for other supported browser types as needed
            default:
                logger.info("Unsupported browser type for Test suite hence terminating the suite runs ");
                System.exit(1);
        }
    }

    // Method to handle download and save to "target/{folderName}/{desiredFilename}"
    public static void handleDownload(Download download, String folderName, String desiredFilename) {
        // Define the base target download folder path
        Path downloadBasePath = Paths.get("target");
        // Construct the full download folder path using the folderName passed from the test
        Path downloadFolderPath = downloadBasePath.resolve(folderName);
        // Ensure the target directory exists
        try {
            if (!Files.exists(downloadFolderPath)) {
                Files.createDirectories(downloadFolderPath);
            }
        } catch (IOException e) {
            System.err.println("Failed to create directory: " + e.getMessage());
            return;
        }
        // Save the file to the target folder with the specified name
        Path downloadPath = downloadFolderPath.resolve(desiredFilename);
        download.saveAs(downloadPath);
        System.out.println("File downloaded and saved to: " + downloadPath.toString());
    }

    public PWBasePage(String givenTitleName) {

        this.pwTitle = givenTitleName;


    }

    public static void setBrowserType(String browserType) {
        PWBasePage.browserType = browserType;
    }

    public static void setDefaultBrowserType() {
        browserType = PropertyLoader.PROP_LIST.getProperty("mentor.qa.browser");
    }

    public void validPage() {

        assertThat(PWBasePage.page).hasTitle(this.pwTitle);


    }

    public static void reInitializePage() {
        browser.close();
        playwright.close();
        playwright = Playwright.create();
        initializeBrowser(browserType);
    }

    public void verifyToastMessage(String expectedText) {
        page.waitForSelector("div.toast-message");
        // Locate the first visible toast message (or specify the exact one if necessary)
        Locator toastMessage = page.locator("div.toast-message").first();
        boolean isVisible = toastMessage.isVisible();
        if (!isVisible) {
            throw new AssertionError("Toast message is not visible!");
        }
        String actualText = toastMessage.textContent();
        logger.info("Toast-Message:{}", actualText);
        assertThat(toastMessage).hasText(expectedText);
        page.waitForTimeout(7000);
    }

    public static String fetchProperty(String key) {
        return PropertyLoader.PROP_LIST.getProperty(key);
    }

    public static String captureScreenshot(String testName, String stackTrace) {
        try {
            File screenshotsDir = new File("target/screenshots");
            if (!screenshotsDir.exists()) {
                screenshotsDir.mkdirs();
            }
            Path screenshotPath = Paths.get("target/screenshots", testName + ".png");
            page.screenshot(new Page.ScreenshotOptions().setPath(screenshotPath));
            BufferedImage image = ImageIO.read(screenshotPath.toFile());
            Graphics2D g = image.createGraphics();
            // Set the font and color for the text
            g.setFont(new Font("Arial", Font.BOLD, 14));
            g.setColor(Color.RED);

            // Split the stack trace into lines
            String[] throwableLines = stackTrace.split("\n");

            // Find the "Call log:" line and include the next line
            StringBuilder relevantLines = new StringBuilder();
            boolean foundRelevantLine = false;

            for (int i = 0; i < throwableLines.length; i++) {
                if (throwableLines[i].contains("Call log:")) {
                    foundRelevantLine = true;
                    // Include the relevant line and the next line
                    relevantLines.append(throwableLines[i]).append("\n");
                    if (i + 1 < throwableLines.length) {
                        relevantLines.append(throwableLines[i + 1]).append("\n");
                    }
                    break;
                }
            }

            if (!foundRelevantLine) {
                // If no relevant line found, include the first two lines of the stack trace
                for (int i = 0; i < Math.min(2, throwableLines.length); i++) {
                    relevantLines.append(throwableLines[i]).append("\n");
                }
            }

            // Split the relevant lines into an array
            String[] relevantLinesArray = relevantLines.toString().split("\n");

            // Calculate the total number of lines to print (test name + relevant lines)
            int totalLines = relevantLinesArray.length + 1; // +1 for the test name

            // Calculate the starting Y position to print the lines at the bottom
            int lineHeight = g.getFontMetrics().getHeight();
            int y = image.getHeight() - (lineHeight * totalLines) - 20;

            // Draw the test name centered at the bottom
            String testNameText = "Test: " + testName;
            int testNameWidth = g.getFontMetrics().stringWidth(testNameText);
            int centerX = (image.getWidth() - testNameWidth) / 2;
            g.drawString(testNameText, centerX, y);
            y += lineHeight; // Move down for the next line

            // Draw each relevant line centered at the bottom
            for (String line : relevantLinesArray) {
                int lineWidth = g.getFontMetrics().stringWidth(line);
                centerX = (image.getWidth() - lineWidth) / 2;
                g.drawString(line, centerX, y);
                y += lineHeight; // Move down for the next line
            }

            // Dispose of the graphics context and save the new image
            g.dispose();
            ImageIO.write(image, "png", screenshotPath.toFile());
            logger.info("Screenshot with exception saved to: " + screenshotPath);
            return screenshotPath.toString();
        } catch (Exception e) {
            logger.error("Failed to capture screenshot with exception: " + e.getMessage(), e);
            return null;
        }
    }

    public String getCurrentDateTime() {
        Date now = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("MMMM d, yyyy hh:mm a");
        return formatter.format(now);
    }

    // Method to get the date formatted after adding minutes
    public String getFutureDateTime(int minutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE, minutes);
        Date futureDate = calendar.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("MMMM d, yyyy hh:mm a");
        return formatter.format(futureDate);
    }

    public static void selectProgramName(String selectProgramName) {
        logger.info("Selecting Program name started");
        page.getByText("Programs", new Page.GetByTextOptions().setExact(true)).click();
        page.getByPlaceholder("Search your program here").click();
        page.getByPlaceholder("Search your program here").fill(selectProgramName);
        page.getByText(selectProgramName, new Page.GetByTextOptions().setExact(true)).click();
        logger.info("Selecting Program name ended");

    }

    public static void accessProject() {
        logger.info("Accessing Project from Program started.");
        try {
            Locator startImprovementBtn = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Start Improvement"));
            assertThat(startImprovementBtn).isVisible();
            startImprovementBtn.click();
        } catch (Exception e) {
            logger.error("Could not find 'Start Improvement' button. Available buttons:");
            page.locator("button").allInnerTexts().forEach(text -> logger.error("Button: " + text));
            throw e;
        }
        Boolean certificate = page.locator("lib-details-page span").filter(new Locator.FilterOptions().setHasText(Pattern.compile("^Certificate$"))).isVisible();
        page.getByText("Task details").click();
        page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("1. Teachers should understand")).click();
        page.locator("#mat-select-value-1").click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName("Completed")).click();
        page.getByRole(AriaRole.BANNER).locator("path").click();
        page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("2. Teachers should implement")).click();
        addSubtasks();
        if (certificate) {
            attachEvidenceAtTaskLevel();
        }
        page.getByRole(AriaRole.BANNER).locator("path").click();
        page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("3. Teachers should implement")).click();
        page.getByText("Not Started").click();
        page.getByText("Completed").click();
        page.getByRole(AriaRole.BANNER).locator("path").click();
        page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("4. Teachers should implement")).click();
        page.getByText("Not Started").click();
        page.getByText("Completed").click();
        if (certificate) {
            attachEvidenceAtTaskLevel();
        }
        page.getByRole(AriaRole.BANNER).locator("path").click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Load more")).click();
        page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("5. Teachers should upload")).click();
        page.getByText("Not Started").click();
        page.getByText("Completed").click();
        page.getByRole(AriaRole.BANNER).locator("path").click();
        addNewTask();

        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Submit Improvement")).click();
        if (certificate) {
            Path png = evidencePath("PNG_Evidence.png");
            Path mp4 = evidencePath("MP4_Evidence.mp4");
            Path pdf = evidencePath("PDF_Evidence.pdf");
            uploadProjectLevelEvidence(png, "Images");
            uploadProjectLevelEvidence(mp4, "Videos");
            uploadProjectLevelEvidence(pdf, "Files");

            page.locator("mat-card").filter(new Locator.FilterOptions().setHasText("Links")).click();
            page.getByLabel("", new Page.GetByLabelOptions().setExact(true)).check();
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Upload").setExact(true)).click();
            page.getByPlaceholder("Add link here").click();
            page.getByPlaceholder("Add link here").fill(fetchProperty("ep.url"));
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Save")).click();
        }
        page.getByRole(AriaRole.BANNER).locator("svg").click();

        downloadActivity();
        syncActivity();
        shareActivity();
        if (certificate) {
            filesActivity();
            page.getByRole(AriaRole.BANNER).locator("path").click();
        }

        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Submit Improvement")).click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Submit Improvement")).click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Submit")).click();

        Locator Syncinglocator = page.getByText("Syncing Project");
            if(Syncinglocator.isVisible()) {
                Syncinglocator.waitFor(
                        new Locator.WaitForOptions()
                                .setState(WaitForSelectorState.HIDDEN)
                                .setTimeout(30000)
                );            }
        assertThat(page.getByText("You have submitted your project successfully")).isVisible();

        if (certificate) {
            logger.info("Certificate is generated for the project");
            checkCertificateGenerated();
        }
        logger.info("Accessing Project from Program ended.");
    }

    public static void addNewTask() {
        logger.info("Adding New Task started.");
        page.getByText("Add your own task.").click();
        page.getByText("Task description").click();
        page.getByLabel("Task description").fill("Task name 1");
        page.getByText("Not Started").click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName("Completed")).locator("span").click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Add task")).click();
        assertThat(page.getByText("New Task has been")).isVisible();
        logger.info("Adding New Task ended.");
    }

    public static void addSubtasks() {
        logger.info("Adding Subtasks started.");
        page.getByPlaceholder("Add subtask name").click();
        page.getByPlaceholder("Add subtask name").fill("Sub task 1");
        page.getByPlaceholder("Add subtask name").press("ArrowLeft");
        page.getByPlaceholder("Add subtask name").press("ArrowRight");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Add subtask")).click();
        page.locator(".sub-task-body > div:nth-child(2) > .mat-mdc-form-field > .mat-mdc-text-field-wrapper > .mat-mdc-form-field-flex > .mat-mdc-form-field-infix").click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName("Completed")).click();
        logger.info("Adding Subtasks ended.");
    }

    public static void downloadActivity() {
        page.locator("lib-icon-list mat-icon").first().click();
        assertThat(page.getByText("You have downloaded the")).isVisible();
    }

    public static void shareActivity() {
        logger.info("Share Activity Project Started.");
        page.locator("lib-icon-list mat-icon").nth(1).click();
        page.getByText("Copy Link").click();
        assertThat(page.getByText("Link copied to clipboard! You")).isVisible();
        logger.info("Share Activity Project ended.");
    }

    public static void filesActivity() {
        logger.info("Check attached files are visible Started.");
        page.locator("lib-icon-list mat-icon").nth(2).click();
        verifyAttachedFiles(1);
        verifyAttachedFiles(2);
        verifyAttachedFiles(3);
        page.getByText("Videos", new Page.GetByTextOptions().setExact(true)).click();
        verifyAttachedFiles(1);
        verifyAttachedFiles(2);
        verifyAttachedFiles(3);
        page.getByText("Files").click();
        verifyAttachedLink(1, "Files");
        verifyAttachedLink(2, "Files");
        verifyAttachedLink(3, "Files");
        page.getByText("Links").click();
        verifyAttachedLink(1, "Links");
        verifyAttachedLink(2, "Links");
        verifyAttachedLink(3, "Links");
        logger.info("Check attached files are visible ended.");
    }

    public static void verifyAttachedFiles(int expectedCount) {
        page.locator("(//div[@class=\"primary-icon cursor-pointer break-words [word-break:break-word]\"])[" + expectedCount + "]").click();
        assertThat(page.locator("//mat-icon[@data-mat-icon-name=\"cancel\" and @type=\"button\"]")).isVisible();
        page.locator("//mat-icon[@data-mat-icon-name=\"cancel\" and @type=\"button\"]").click();
    }

    public static void verifyAttachedLink(int expectedCount, String typeOfFile) {
        Page mainPage = page;
        Page newTab = page.waitForPopup(() -> {
            page.locator("(//div[@class='primary-icon cursor-pointer break-words [word-break:break-word]'])[" + expectedCount + "]"
            ).click();
        });
        newTab.waitForLoadState(LoadState.DOMCONTENTLOADED);
        if (Objects.equals(typeOfFile, "Links")) {
            assertTrue(newTab.url().contains(fetchProperty("ep.url")));
        }
        newTab.close();
    }

    public static void syncActivity() {
        logger.info("Sync Activity Project Started.");
        page.locator("lib-icon-list mat-icon").nth(3).click();
        Locator syncingText = page.getByText("Syncing Project").nth(1);
        assertThat(syncingText).isHidden(
                new LocatorAssertions.IsHiddenOptions().setTimeout(30000)
        );
        Locator syncToast = page.getByText("Your project has been synced");

        syncToast.waitFor(
                new Locator.WaitForOptions()
                        .setState(WaitForSelectorState.VISIBLE)
                        .setTimeout(15000)
        );

        assertThat(syncToast).isVisible();
        logger.info("Sync Activity Project ended.");
    }

    public static void attachEvidenceAtTaskLevel() {
        logger.info("Attaching Evidence for Project Started.");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Add files")).click();
        page.getByLabel("", new Page.GetByLabelOptions().setExact(true)).check();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Upload").setExact(true)).click();

        Path png = evidencePath("PNG_Evidence.png");
        Path pdf = evidencePath("PDF_Evidence.pdf");
        Path mp4 = evidencePath("MP4_Evidence.mp4");

        uploadTaskLevelEvidence(png, "Images");
        uploadTaskLevelEvidence(mp4, "Videos");
        uploadTaskLevelEvidence(pdf, "Files");
        page.locator("mat-card").filter(new Locator.FilterOptions().setHasText("Links")).click();
        page.getByPlaceholder("Add link here").click();
        page.getByPlaceholder("Add link here").fill(fetchProperty("ep.url"));
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Save")).click();

        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Attach files")).click();
        assertThat(page.getByText("Files attached successfully")).isVisible();
        logger.info("Attaching Evidence for Project ended.");

    }

    public static void uploadTaskLevelEvidence(Path filePath, String fileType) {
        FileChooser fileChooser = page.waitForFileChooser(() -> {
            page.locator("mat-card").filter(new Locator.FilterOptions().setHasText(fileType)).click();
        });
        fileChooser.setFiles(filePath);
        assertThat(page.getByText("Attached Successfully")).isVisible();
    }

    public static void uploadProjectLevelEvidence(Path filePaths, String fileType) {
        FileChooser fileChooser = page.waitForFileChooser(() -> {
            page.locator("mat-card").filter(new Locator.FilterOptions().setHasText(fileType)).click();
            page.getByLabel("", new Page.GetByLabelOptions().setExact(true)).check();
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Upload").setExact(true)).click();
        });
        fileChooser.setFiles(filePaths);
        assertThat(page.getByText("Attached Successfully")).isVisible();
    }

    public static Path evidencePath(String fileName) {
        return Paths.get(
                System.getProperty("user.dir"),
                "src", "main", "resources", "AllEvidence", fileName
        );
    }

    public static void checkCertificateGenerated() {
        page.locator("lib-icon-list mat-icon").nth(2).click();
        page.reload();
        Locator certificateLocator = page.locator("#stateTitle");
        assertThat(certificateLocator)
                .isVisible(new LocatorAssertions.IsVisibleOptions().setTimeout(30000));
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Download")).click();
        Download download = page.waitForDownload(() -> {
            page.getByRole(AriaRole.MENUITEM, new Page.GetByRoleOptions().setName("PDF")).click();
        });
        page.getByText("Certificate is getting").click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Download")).click();
        Download download1 = page.waitForDownload(() -> {
            page.getByRole(AriaRole.MENUITEM, new Page.GetByRoleOptions().setName("PNG")).click();
        });
        page.getByText("Certificate is getting").click();
        page.getByRole(AriaRole.BANNER).locator("svg").click();
    }

//    Project: code ends, Observation: code starts here.
        public static void  accessingObservationWithRubric() {
            logger.info("Submitting an Observation With Rubric started.");
            page.locator("(//mat-card-content[@class=\"mat-mdc-card-content observation-content\"])[1]").click();
            page.locator("div").filter(new Locator.FilterOptions().setHasText(Pattern.compile("^0%$"))).nth(1).click();
            page.getByText("Planning & Execution").click();
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Start")).click();
            page.getByPlaceholder("Enter your response").fill("Stud name");
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Add remarks")).click();
            page.getByPlaceholder("Add a remark").fill("Remarks");
            attachEvidenceForSurveyService();
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Submit")).click();
            page.locator("(//button[@class=\"btn btnMargin ng-star-inserted\"])[1]").click();

            page.locator("div").filter(new Locator.FilterOptions().setHasText(Pattern.compile("^0%$"))).nth(1).click();
            page.getByText("Data based Governance").click();
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Start")).click();
            page.getByLabel("Male", new Page.GetByLabelOptions().setExact(true)).check();
            page.getByLabel("IAF").check();
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("2")).click();
            page.getByPlaceholder("Enter your response").fill("Selector");
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Add remarks")).click();
            page.getByPlaceholder("Add a remark").fill("Remarks");
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Submit")).click();
            page.locator("(//button[@class=\"btn btnMargin ng-star-inserted\"])[1]").click();
//            page.locator("//button[contains(@class,'back-button mdc-icon-button mat-mdc-icon-button mat-unthemed mat-mdc-button-base')]").click();

            Locator syncingText = page.getByText("Uploading").nth(1);
            assertThat(syncingText).isHidden(
                    new LocatorAssertions.IsHiddenOptions().setTimeout(30000)
            );
            assertThat(page.getByText("Your observation has been")).isVisible();
            logger.info("Submitting an Observation With Rubric ended.");
        }

        public static void attachEvidenceForSurveyService() {
            logger.info("Attaching Evidence for Observation Started.");
            Path png = evidencePath("PNG_Evidence.png");
            Path pdf = evidencePath("PDF_Evidence.pdf");
            Path mp4 = evidencePath("MP4_Evidence.mp4");
            fileChooserForSurveyService(png, 0); // First 'Add file' button
            fileChooserForSurveyService(mp4, 1); // Second 'Add file' button
            fileChooserForSurveyService(pdf, 2); // Third 'Add file' button
            logger.info("Attaching Evidence for Observation ended.");
        }

        public static void fileChooserForSurveyService(Path filePath, int buttonIndex) {
            Locator addFileButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Add file")).nth(buttonIndex);
            if (addFileButton.isVisible()) {
                FileChooser fileChooser = page.waitForFileChooser(() -> {
                    addFileButton.click();
                    page.getByLabel("", new Page.GetByLabelOptions().setExact(true)).check();
                    page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Upload").setExact(true)).click();
                });
                fileChooser.setFiles(filePath);
                assertThat(page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Ok"))).isVisible();
                page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Ok")).click();
            }
        }

        public static void observeAgain(int observationCount, boolean withRubric) {
            logger.info("Observe Again action started.");
            for(int i=1; i<=observationCount; i++) {
                page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Observe again")).click();
                page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Yes")).click();

                if (withRubric) {
                    accessingObservationWithRubric();
                } else {
                    accessingObservationWithOUTRubric();
                }
            }
            logger.info("Observe Again action ended.");
        }

        public static void addEntity(String EntityName, boolean withRubric) {
            logger.info("Adding Entity started.");
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Add school")).click();
            page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Search school")).fill(EntityName);
            page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Search school")).click();
            page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(EntityName)).locator("div").first().click();
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Add")).click();
            page.getByText(EntityName).click();
            if (withRubric) {
                accessingObservationWithRubric();
            } else {
                accessingObservationWithOUTRubric();
            }
            logger.info("Adding Entity ended.");
        }

        public static void accessingObservationWithOUTRubric() {
            logger.info("Submitting an Observation Without Rubric started.");
            page.locator("(//mat-card-content[@class=\"mat-mdc-card-content observation-content\"])[1]").click();
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Start")).click();
            page.getByLabel("KARNATAKA PUBLIC SCHOOLS GHPS").check();
            page.getByLabel("Open calendar").click();
            page.getByLabel("Choose month and year").click();
            page.getByLabel("2026").click();
            page.getByLabel("Jan").click();
            page.getByLabel("11 January").click();
            page.getByText("Yes", new Page.GetByTextOptions().setExact(true)).first().click();
            page.getByText("Yes", new Page.GetByTextOptions().setExact(true)).nth(1).click();
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("2")).click();
            page.getByRole(AriaRole.RADIO, new Page.GetByRoleOptions().setName("Yes").setExact(true)).check();
            page.getByLabel("Yes, a fully equipped science").check();
            attachEvidenceForSurveyService();
            page.getByLabel("YouTube").check();
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("3")).click();
            page.getByPlaceholder("Enter your response").fill("2");
            page.getByRole(AriaRole.RADIO, new Page.GetByRoleOptions().setName("Yes")).check();
            page.getByPlaceholder("Enter your response").click();
            page.getByRole(AriaRole.SLIDER).fill("5");
            page.getByRole(AriaRole.RADIO, new Page.GetByRoleOptions().setName("Yes")).check();
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Submit")).click();
            page.locator("(//button[@class=\"btn btnMargin ng-star-inserted\"])[1]").click();
            // Wait up to 15 seconds for the success message to appear
            Locator successMsg = page.locator("text=Your observation has been");
            successMsg.waitFor(new Locator.WaitForOptions().setTimeout(15000).setState(WaitForSelectorState.VISIBLE));
            assertThat(successMsg).isVisible();
            logger.info("Submitting an Observation Without Rubric ended.");
        }

        public void accessingSurvey() {
            logger.info("Submitting a Survey (from Program) started.");
            page.getByLabel("Always").check();
            attachEvidenceForSurveyService();
            page.getByLabel("Internet videos").check();
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Add remarks")).first().click();
            page.getByPlaceholder("Add a remark").fill("Remarks");
            page.getByLabel("Group discussions").check();
            page.getByLabel("Very confident").check();
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("2")).click();
            page.getByLabel("Lack of materials").check();
            page.getByLabel("Lack of training").check();
            page.getByLabel("Participate with support").check();
            page.getByLabel("Significant improvement").check();
            page.getByRole(AriaRole.SLIDER).fill("6");
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Submit")).click();
            page.locator("(//button[@class=\"btn btnMargin ng-star-inserted\"])[1]").click();
            Locator syncingText = page.getByText("Uploading").nth(1);
            assertThat(syncingText).isHidden(
                    new LocatorAssertions.IsHiddenOptions().setTimeout(30000)
            );
            assertThat(page.getByText("Your survey has been")).isVisible();
            logger.info("Submitting a Survey (from Program) ended.");
        }
}