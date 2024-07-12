package org.shikshalokam.Listerners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.*;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.shikshalokam.uiPageObjects.PWBasePage.captureScreenshot;


public class SLCustomListener implements ITestListener  {
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
        Throwable throwable = result.getThrowable();
        logger.info("Test failed: " + testName, throwable);

        // Convert the throwable stack trace to a string
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        throwable.printStackTrace(pw);
        String stackTrace = sw.toString();
        captureScreenshot(testName, stackTrace);
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
