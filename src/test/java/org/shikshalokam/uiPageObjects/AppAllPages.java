package org.shikshalokam.uiPageObjects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AppAllPages {

    private static final Logger logger = LogManager.getLogger(AppAllPages.class);
    public static AppLoginPage loginPage = new AppLoginPage("MentorED");
    public static AppSignupPage SignupPage = new AppSignupPage("MentorED");
    public static BecomeMentorPage mentorPage =new BecomeMentorPage("MentorED");
<<<<<<< Updated upstream

=======
    public static AppWelcomePage welcomePage =new AppWelcomePage("MentorED");
    public static AppWorkspacePage workspacePage =new AppWorkspacePage("MentorED");
    public static AppProfilePage updateProfilePage =new AppProfilePage("MentorED");
>>>>>>> Stashed changes
}
