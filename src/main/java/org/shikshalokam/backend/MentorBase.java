package org.shikshalokam.backend;
import org.apache.logging.log4j.core.config.Configurator;

public class MentorBase {
    static{
        PropertyLoader.loadProperties();
        Configurator.initialize(null, System.getProperty("user.dir")+File.separator+ Paths.get("src","main","resources","config").toString()+File.separator+"log4j2.properties");
    }


}
