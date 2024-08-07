package org.shikshalokam.uiPageObjects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AppAllPages {

    private static final Logger logger = LogManager.getLogger(AppAllPages.class);
    public static AppLoginPage loginPage = new AppLoginPage("MentorED");
    public static AppSignupPage signupPage = new AppSignupPage("MentorED");
    public static AppBecomeMentorPage mentorPage =new AppBecomeMentorPage("MentorED");
    public static AppWelcomePage welcomePage =new AppWelcomePage("MentorED");
    public static AppWorkspacePage workspacePage =new AppWorkspacePage("MentorED");
    public static AppProfilePage profileDetailsPage =new AppProfilePage("MentorED");
    public static AppChangePasswordPage changePasswordPage =new AppChangePasswordPage("MentorED");
    public static AppResetPasswordPage resetPasswordPage =new AppResetPasswordPage("MentorED");
    public static AppCreateSessionPage createSessionPage =new AppCreateSessionPage("MentorED");
    public static AppSessionDetailsPage sessionDeatilsPage =new AppSessionDetailsPage("MentorED");
    public static AppMentorsPage mentorsPage =new AppMentorsPage("MentorED");


}





