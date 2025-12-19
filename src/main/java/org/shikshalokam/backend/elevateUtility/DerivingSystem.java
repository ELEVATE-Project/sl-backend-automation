package org.shikshalokam.backend.elevateUtility;

import static org.shikshalokam.backend.elevateUtility.DeleteUserSAAS.fetchProperty;

public class DerivingSystem {
    public static boolean SelectTenants(){
        String URL = fetchProperty("ep.url");
        String searchString = "shikshagraha";
        return URL.contains(searchString);
    }
}
