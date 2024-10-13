package org.shikshalokam.backend.ep;

import org.testng.annotations.Test;

public class Testelevatelogin extends ElevateProjectBaseTest{

    @Test
    public void log(){

        logintoElevate("smile@guerrillamail.info","Password@1234");
    }
}
