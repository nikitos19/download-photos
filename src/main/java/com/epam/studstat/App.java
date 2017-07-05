package com.epam.studstat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.sql.SQLException;
import java.util.Properties;

public class App {

    private static void validate(String[] propAndAvatarsPath) throws FileNotFoundException {
        if(propAndAvatarsPath.length != 2){
            throw new InvalidParameterException("It must be two parameters");
        }

        File propFile = new File(propAndAvatarsPath[0]);
        if (!propFile.isFile() || !propFile.exists() || !propFile.getName().endsWith(".properties")) {
            throw new FileNotFoundException("Properties file isn't found");
        }

        File avatarsDir = new File(propAndAvatarsPath[1]);
        if (!avatarsDir.isDirectory() || !avatarsDir.exists()) {
            throw new FileNotFoundException("Directory isn't found");
        }
    }

    public static void main(String[] propAndAvatarsPath) throws IOException, SQLException, ClassNotFoundException {
        validate(propAndAvatarsPath);

        Properties properties = new ApplicationProperties().getProperties(propAndAvatarsPath[0]);

        Properties connProp = new Properties();
        connProp.setProperty("user", properties.getProperty("spring.datasource.username"));
        connProp.setProperty("password", properties.getProperty("spring.datasource.password"));

        ExportPhotos export = new ExportPhotos();
        export.savePhotos(propAndAvatarsPath[1], export.getPersonsPhotos(
                properties.getProperty("spring.datasource.url"),
                connProp,
                properties.getProperty("spring.datasource.driver-class-name")));
    }

}
