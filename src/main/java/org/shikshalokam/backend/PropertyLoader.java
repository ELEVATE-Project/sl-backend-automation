package org.shikshalokam.backend;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyLoader {

    public static Properties PROP_LIST = new Properties();
    

    public static Properties loadProperties() {
        String currentDirectory = System.getProperty("user.dir");
        System.out.println("Current directory: " + currentDirectory);
        String filePath = currentDirectory+"\\src\\main\\resources\\config\\automation.properties";
        System.out.println(filePath);
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
