package com.epam.studstat;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

public class App {

    public static void main(String[] args) throws IOException, SQLException, ClassNotFoundException {
        MainProperties appProperties = new MainProperties();
        Properties properties = appProperties.getProperties();

        String url = properties.getProperty("spring.datasource.url");
        String driver = properties.getProperty("spring.datasource.driver-class-name");
        String username = properties.getProperty("spring.datasource.username");
        String password = properties.getProperty("spring.datasource.password");

        Properties connProp = new Properties();
        connProp.setProperty("user", username);
        connProp.setProperty("password", password);

        ExportPhotos export = new ExportPhotos();
        export.savePhotos(export.getPersonsPhotos(url, connProp, driver));
    }

}
