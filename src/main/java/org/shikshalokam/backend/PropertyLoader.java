package org.shikshalokam.backend;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyLoader {

    public static Properties PROP_LIST = new Properties();
    private static final Logger logger = LogManager.getLogger(PropertyLoader.class);

    public static Properties loadProperties() {
        String currentDirectory = System.getProperty("user.dir");
        System.out.println("Current directory: " + currentDirectory);
        String filePath=null;
        if (System.getProperty("automationPropertiesFile")==null) {
            filePath = currentDirectory + "\\src\\main\\resources\\config\\automation.properties";
            System.out.println("The file path from if flow :" + filePath);
        }else
        {

            filePath=System.getProperty("automationPropertiesFile");
            logger.info("The -D Parameter file location:"+ filePath);
            System.out.println("The file path from else flow :" + filePath);
        }
        try {
            FileInputStream propFile = new FileInputStream(filePath);
            PROP_LIST.load(propFile);
        }catch (IOException e)
        {
            System.out.println( "Property file not found : Please check the Resource/config folder");
        }

      return PROP_LIST;
    }
}
