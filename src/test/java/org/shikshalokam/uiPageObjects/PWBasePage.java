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

    public static void captureScreenshot(String testName, Exception exception) {
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
            // Draw the exception message onto the image
            String exceptionMessage = exception.getMessage();
            int x = 10;
            int y = image.getHeight() - 10; // Bottom of the image
            g.drawString(exceptionMessage, x, y);
            // Dispose of the graphics context and save the new image
            g.dispose();
            ImageIO.write(image, "png", screenshotPath.toFile());
            System.out.println("Screenshot with exception saved to: " + screenshotPath.toString());
        } catch (Exception e) {
            System.err.println("Failed to capture screenshot with exception: " + e.getMessage());
        }
    }
}








