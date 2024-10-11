package org.shikshalokam.backend.ep;

import org.testng.annotations.Test;

public class Testelevatelogin extends ElevateProjectBaseTest{

    @Test
    public void log(){

        logintoElevate("manju@guerrillamail.info","Password@123");
    }
}
