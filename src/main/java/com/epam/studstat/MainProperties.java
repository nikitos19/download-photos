package com.epam.studstat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

class MainProperties {

    Properties getProperties() throws IOException {
        File jarPath = new File(App.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        String propertiesPath = jarPath.getParentFile().getAbsolutePath() + "/application.properties";
        File appFile = new File(propertiesPath);

        Properties appProp = new Properties();
        if (appFile.exists()) {
            try (FileInputStream fis = new FileInputStream(appFile)) {
                appProp.load(fis);
            }
            return appProp;
        }
        throw new FileNotFoundException(appFile.getAbsolutePath() + " not found");
    }

}
