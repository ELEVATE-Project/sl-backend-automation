package Listerners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.*;

import static org.shikshalokam.uiPageObjects.PWBasePage.captureScreenshot;

public class SLCustomListener implements ITestListener {
    private static final Logger logger = LogManager.getLogger(SLCustomListener.class);

    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getName();
        logger.info("Test started: " + testName);
    }

    public void onTestSuccess(ITestResult result) {
        String testName = result.getName();
        logger.info("Test succeeded: " + testName);
    }

    public void onTestFailure(ITestResult result) {
        String testName = result.getName();
        Throwable exception = result.getThrowable();
        logger.error("Test failed: " + testName, exception);
        captureScreenshot(testName, (Exception) exception);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        String testName = result.getName();
        logger.warn("Test skipped: " + testName);

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
