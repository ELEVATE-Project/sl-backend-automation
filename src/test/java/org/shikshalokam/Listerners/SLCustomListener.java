package org.shikshalokam.Listerners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.shikshalokam.backend.PropertyLoader;
import org.testng.*;

import static org.shikshalokam.uiPageObjects.PWBasePage.PWBrowser;
import static org.shikshalokam.uiPageObjects.PWBasePage.captureScreenshot;

public class SLCustomListener implements ITestListener, ISuiteListener {

    private static final Logger logger = LogManager.getLogger(SLCustomListener.class);
    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    @Override
    public void onStart(ISuite suite) {
        logger.info("Suite started: " + suite.getName());

        ExtentSparkReporter htmlReporter = new ExtentSparkReporter("target/ExtentReport.html");
        htmlReporter.config().setTheme(Theme.DARK);
        htmlReporter.config().setDocumentTitle("Automation Test Report");
        htmlReporter.config().setReportName("Test Execution Report");

        extent = new ExtentReports();
        extent.attachReporter(htmlReporter);
        if (Boolean.parseBoolean(PropertyLoader.PROP_LIST.getProperty("sl.capture.screenshot.on.failure"))) {
            extent.setSystemInfo("Browser", String.valueOf(PWBrowser));
        }
        // Load environment property and set it in the Extent report as "ENVIRONMENT"
        String environment = PropertyLoader.PROP_LIST.getProperty("environment", "Unknown Environment");
        extent.setSystemInfo("ENVIRONMENT", environment);
        extent.setSystemInfo("OS", System.getProperty("os.name"));
        extent.setSystemInfo("OS Version", System.getProperty("os.version"));
        extent.setSystemInfo("Java Version", System.getProperty("java.version"));
    }

    @Override
    public void onFinish(ISuite suite) {
        logger.info("Suite finished: " + suite.getName());
        extent.flush();
    }

    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String description = result.getMethod().getDescription();
        String className = result.getTestClass().getName();
        logger.info("Test started: " + className + " - " + testName);

        ExtentTest extentTest = extent.createTest(className + " - " + testName)
                .assignCategory(className)
                .info("Description: " + description);
        test.set(extentTest);

        result.setAttribute("startTime", System.currentTimeMillis());

        Object[] parameters = result.getParameters();
        if (parameters.length > 0) {
            StringBuilder params = new StringBuilder("Parameters: ");
            for (Object param : parameters) {
                params.append(param.toString()).append(", ");
            }
            test.get().info(params.toString());
        }
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        long startTime = (long) result.getAttribute("startTime");
        long duration = System.currentTimeMillis() - startTime;
        String testName = result.getMethod().getMethodName();
        logger.info("Test succeeded: " + testName + " (Duration: " + duration + " ms)");
        test.get().pass("Test passed (Duration: " + duration + " ms)");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        long startTime = (long) result.getAttribute("startTime");
        long duration = System.currentTimeMillis() - startTime;
        String testName = result.getMethod().getMethodName();
        Throwable throwable = result.getThrowable();

        logger.error("Test failed: " + testName + " (Duration: " + duration + " ms)");
        if (throwable instanceof AssertionError) {
            test.get().fail("Assertion failed: " + throwable.getMessage());
        } else {
            test.get().fail("Exception occurred: " + throwable.getMessage());
        }

        // Capture screenshot on failure
        if (Boolean.parseBoolean(PropertyLoader.PROP_LIST.getProperty("sl.capture.screenshot.on.failure"))) {
            String screenshotPath = captureScreenshot(result.getMethod().getMethodName(), throwable.toString());
            test.get().addScreenCaptureFromPath(screenshotPath);
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        logger.warn("Test skipped: " + testName);
        test.get().skip("Test skipped");
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {

    }

    @Override
    public void onStart(ITestContext context) {

    }

    @Override
    public void onFinish(ITestContext context) {

    }
}
