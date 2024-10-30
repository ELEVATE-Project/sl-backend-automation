package org.shikshalokam.backend;
import org.apache.logging.log4j.core.config.Configurator;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;

public class MentorBase {
    static{
        PropertyLoader.loadProperties();
        Configurator.initialize(null, System.getProperty("user.dir")+File.separator+ Paths.get("src","main","resources","config").toString()+File.separator+"log4j2.properties");
        GmailAPI.deleteEmails();
    }

    public static URI createURI(String endpoint) {
        try {
            return new URI(endpoint);
        } catch (URISyntaxException e) {
             throw new RuntimeException("Invalid URI Syntax", e);
        }
    }

}
