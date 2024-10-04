# MentorED
**Please follow the steps below to run the automation tests for the project. The tests can be run in both QA and Production environments, depending on the configuration you select.**

Steps to Run Automation
1.Clone the Latest Code: https://github.com/ELEVATE-Project/sl-backend-automation

2.Credentials File:

    Ensure that you have the credentials file (credentials_new.json) added to the project.
    Path to add the above file : sl-backend-automation/src/main/resources/credentials_new.json

3.Switch Between QA and Production:

    You can switch between QA and Production environments by modifying the automation property files.
    Property file : resources/config/automation.properties

4.Run the Automation Tests:

    Open a terminal in the root project folder and use the following Maven command to run the automation tests:
    cmd : mvn clean install -DsuiteXmlFile="path/to/suite.xml"

Portal : mvn clean install -DsuiteXmlFile="sl-backend-automation/UISanitySuite.xml"   [ Windows ]
Portal : mvn test -Dsurefire.suiteXmlFiles=/home/dell/IdeaProjects/sl-backend-automation/UISanitySuite.xml  [ Ubuntu ]
PWA : mvn clean install -DsuiteXmlFile="sl-backend-automation/UISanitySuitePixel4a.xml"

5.Report Generation:

    After the test run is complete, the report will be generated under the following path: sl-backend-automation/target/ExtentReport.

**Note: Prerequisites for Local Setup**
Before running the automation tests locally, ensure the following prerequisites are set up:

    Java Development Kit (JDK)
    Maven: Install and configure Apache Maven to manage project dependencies.
    IDE: Use IntelliJ IDEA Community Edition for running and editing the project.
    GitHub Desktop: Ensure GitHub Desktop is installed for cloning the repository.
    Valid Credentials File: Ensure the credentials_new.json file is correctly placed in the project.
