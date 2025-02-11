package org.shikshalokam.uiPageObjects;

import com.microsoft.playwright.*;
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
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;


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


}