package org.shikshalokam.uiPageObjects.ep;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.shikshalokam.uiPageObjects.PWBasePage;

public class AppSubmitSurveyDeeplink extends PWBasePage {
    AppSubmitSurveyDeeplink appSubmitSurveyDeeplink;
    private static final Logger logger = LogManager.getLogger(AppHomePage.class);

    public AppSubmitSurveyDeeplink(String givenTitleName) {
        super(givenTitleName);
        this.appSubmitSurveyDeeplink = this;
    }

    public AppSubmitSurveyDeeplink submitsurveyDeeplink() {
        logger.info("Submitting survey via deeplink started.");
        page.navigate(fetchProperty("ep.surveyDeeplink"));
        page.waitForLoadState();
        accessingSurvey();
        logger.info("Submitting survey via deeplink ended.");
        return appSubmitSurveyDeeplink;
    }
}