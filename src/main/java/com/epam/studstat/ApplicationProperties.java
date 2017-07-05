package com.epam.studstat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

class ApplicationProperties {

    Properties getProperties(String propertiesPath) throws IOException {
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
