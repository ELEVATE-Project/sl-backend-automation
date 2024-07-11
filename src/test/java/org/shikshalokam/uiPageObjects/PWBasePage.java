package org.shikshalokam.uiPageObjects;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.shikshalokam.backend.MentorEDBaseTest;
import org.shikshalokam.backend.PropertyLoader;
import com.microsoft.playwright.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.testng.Assert.assertEquals;


public class PWBasePage extends MentorEDBaseTest {
    private static final Logger logger = LogManager.getLogger(PWBasePage.class);
    public static BrowserContext browserContext;
    public static Page page;
    public static PWBrowser PWBrowser;
    public static Playwright playwright = Playwright.create();
    public static Browser browser;
    private String pwTitle;


    public static Boolean headless;

    static {

        headless = true;
        if (PropertyLoader.PROP_LIST.getProperty("mentor.qa.browser.setHeadless").equals("false")) {
            headless = false;
        }
        switch (PropertyLoader.PROP_LIST.getProperty("mentor.qa.browser")) {
            case "chromium":
                logger.info("Using Chromium browser for Test suite ");
                PWBasePage.PWBrowser = PWBrowser.chromium;
                browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(headless));
                browserContext = browser.newContext(new Browser.NewContextOptions().setViewportSize(null));
                page = browserContext.newPage();
                break;
            case "msedge":
                logger.info("Using Edge browser for Test suite ");
                PWBasePage.PWBrowser = PWBrowser.msedge;
                browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setChannel("msedge").setHeadless(headless));
                browserContext = browser.newContext();
                page = browserContext.newPage();
                break;
            case "firefox":
                logger.info("Using firefox browser for Test suite");
                PWBasePage.PWBrowser = PWBrowser.firefox;
                browser = playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(headless));
                browserContext = browser.newContext();
                page = browserContext.newPage();
                break;
            case "webkit":
                logger.info("Using Safri browser for Test suite");
                PWBasePage.PWBrowser = PWBrowser.webkit;
                browser = playwright.webkit().launch(new BrowserType.LaunchOptions().setHeadless(headless));
                browserContext = browser.newContext();
                page = browserContext.newPage();
                break;
            // Add cases for other supported browser types as needed
            default:
                logger.info("Unsupported browser type for Test suite hence terminating the suite runs ");
                System.exit(1);
        }
    }

    public PWBasePage(String givenTitleName) {

        this.pwTitle = givenTitleName;


    }

    public void validPage() {

        assertThat(PWBasePage.page).hasTitle(this.pwTitle);


    }

    public static void reInitializePage() {
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(headless));
        browserContext = browser.newContext(new Browser.NewContextOptions().setViewportSize(null));
        page = browserContext.newPage();
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
        assertEquals(expectedText, actualText, "Text does not match!");
    }

    public static String fetchProperty(String key) {
        return PropertyLoader.PROP_LIST.getProperty(key);
    }

    public static void captureScreenshot(String testName, Throwable throwable) {
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

            // Convert the throwable stack trace to a string
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            throwable.printStackTrace(pw);
            String throwableStackTrace = sw.toString();

            // Split the stack trace into lines
            String[] throwableLines = throwableStackTrace.split("\n");

            // Find the Call log: line and include the next line
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
            String testNameText = "TestName: " + testName;
            int testNameWidth = g.getFontMetrics().stringWidth(testNameText);
            int centerX = (image.getWidth() - testNameWidth) / 2;
            g.drawString(testNameText, centerX, y);
            y += lineHeight; // Move down for the next line

            // Draw each relevant line centered at the bottom
            for (String line : relevantLinesArray) {
                g.drawString(line, 10, y); // Left-align the text at x = 10
                y += lineHeight; // Move down for the next line
            }

            // Dispose of the graphics context and save the new image
            g.dispose();
            ImageIO.write(image, "png", screenshotPath.toFile());
            System.out.println("Screenshot with exception saved to: " + screenshotPath);
        } catch (Exception e) {
            System.err.println("Failed to capture screenshot with exception: " + e.getMessage());
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








