package org.shikshalokam.backend;
import org.apache.logging.log4j.core.config.Configurator;

public class MentorBase {
    static{
        PropertyLoader.loadProperties();
        Configurator.initialize(null, System.getProperty("user.dir")+"\\src\\main\\resources\\config\\log4j2.properties");
    }


}
