package org.shikshalokam.backend;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class TestMyCode extends MentorEDBaseTest {
    @BeforeTest
    public void init()
    {
        loginToMentorED("subashgryles@gmail.com","password");
    }
    @Test
    public void mySample()
    {
        SMsampleCSVBulkPuload.uploadSampleFileToCloud();
    }
}
