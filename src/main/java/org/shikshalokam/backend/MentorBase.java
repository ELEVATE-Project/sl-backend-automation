package org.shikshalokam.backend;
import org.apache.logging.log4j.core.config.Configurator;
import org.shikshalokam.backend.elevateUtility.CommonUtilitySAAS;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;

import static org.shikshalokam.backend.elevateUtility.CommonUtilitySAAS.fetchProperty;

public class MentorBase {
    static{
        PropertyLoader.loadProperties();
        Configurator.initialize(null, System.getProperty("user.dir")+File.separator+ Paths.get("src","main","resources","config").toString()+File.separator+"log4j2.properties");
        GmailAPI.deleteEmails();
        CommonUtilitySAAS.deleteUser(fetchProperty("ep.mail"), fetchProperty("ep.password"),fetchProperty("ep.superadminmail"), fetchProperty("ep.superadminpassword"));
    }

    public static URI createURI(String endpoint) {
        try {
            return new URI(endpoint);
        } catch (URISyntaxException e) {
             throw new RuntimeException("Invalid URI Syntax", e);
        }
    }

}
