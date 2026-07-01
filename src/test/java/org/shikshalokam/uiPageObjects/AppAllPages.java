package org.shikshalokam.uiPageObjects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.shikshalokam.uiPageObjects.ep.*;

import static org.shikshalokam.uiPageObjects.PWBasePage.fetchProperty;

public class AppAllPages {

    private static final Logger logger = LogManager.getLogger(AppAllPages.class);
    public static AppLoginPage loginPage = new AppLoginPage("MentorED");
    public static AppSignupPage signupPage = new AppSignupPage("MentorED");
    public static AppBecomeMentorPage mentorPage = new AppBecomeMentorPage("MentorED");
    public static AppWelcomePage welcomePage = new AppWelcomePage("MentorED");
    public static AppWorkspacePage workspacePage = new AppWorkspacePage("MentorED");
    public static AppProfilePage profileDetailsPage = new AppProfilePage("MentorED");
    public static AppChangePasswordPage changePasswordPage = new AppChangePasswordPage("MentorED");
    public static AppResetPasswordPage resetPasswordPage = new AppResetPasswordPage("MentorED");
    public static AppCreateSessionPage createSessionPage = new AppCreateSessionPage("MentorED");
    public static AppSessionDetailsPage sessionDeatilsPage = new AppSessionDetailsPage("MentorED");
    public static AppMentorsPage mentorsPage = new AppMentorsPage("MentorED");
    public static AppManageSessionPage manageSessionPage = new AppManageSessionPage("MentorED");

    public static AppHomePage homePage = new AppHomePage(fetchProperty("ep.AppHomePage.title"));
    public static AppEPLoginPage eploginpage = new AppEPLoginPage(fetchProperty("ep.AppEPLoginPage.title"));
    public static AppRegistrationPage registration = new AppRegistrationPage(fetchProperty("ep.AppRegistrationPage.title"));
    public static AppProgramPage programPage =new AppProgramPage(fetchProperty("ep.AppProgramPage.title"));

    // New page objects for program actions
    public static AppSubmitProjectFromProgram submitProjectFromProgram = new AppSubmitProjectFromProgram(fetchProperty("ep.AppProgramPage.title"));
    public static AppSubmitObservationWithRubricFromProgram submitObservationWithRubricFromProgram = new AppSubmitObservationWithRubricFromProgram(fetchProperty("ep.AppProgramPage.title"));
    public static AppSubmitObservationWithoutRubricFromProgram submitObservationWithoutRubricFromProgram = new AppSubmitObservationWithoutRubricFromProgram(fetchProperty("ep.AppProgramPage.title"));
    public static AppSubmitSurveyFromProgram submitSurveyFromProgram = new AppSubmitSurveyFromProgram(fetchProperty("ep.AppProgramPage.title"));

    // New page objects for tile actions
    public static AppSubmitProjectFromTile submitProjectFromTile = new AppSubmitProjectFromTile(fetchProperty("ep.AppProgramPage.title"));
    public static AppSubmitObservationWithRubricFromTile submitObservationWithRubricFromTile = new AppSubmitObservationWithRubricFromTile(fetchProperty("ep.AppProgramPage.title"));
    public static AppSubmitObservationWithoutRubricFromTile submitObservationWithoutRubricFromTile = new AppSubmitObservationWithoutRubricFromTile(fetchProperty("ep.AppProgramPage.title"));
    public static AppSubmitSurveyFromTile submitSurveyFromTile = new AppSubmitSurveyFromTile(fetchProperty("ep.AppProgramPage.title"));

    // Deep links
    public static AppSubmitProjectDeeplink submitProjectDeeplink = new AppSubmitProjectDeeplink(fetchProperty("ep.AppProgramPage.title"));
    public static AppSubmitObservationWithRubricDeeplink observationWithRubricDeeplink = new AppSubmitObservationWithRubricDeeplink(fetchProperty("ep.AppProgramPage.title"));
    public static AppSubmitObservationWithOutRubricDeeplink observationWithOutRubricDeeplink = new AppSubmitObservationWithOutRubricDeeplink(fetchProperty("ep.AppProgramPage.title"));
    public static AppSubmitSurveyDeeplink submitSurveyDeeplink = new AppSubmitSurveyDeeplink(fetchProperty("ep.AppProgramPage.title"));

    // LED IMP Flow
    public static AppObservationLEDIMPFlowPage observationLEDIMPFlowPage = new AppObservationLEDIMPFlowPage(fetchProperty("ep.AppProgramPage.title"));
}
