package com.epam.studstat;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Main {

    public static Properties getProperties() throws IOException {
        File appFile = new File("./application.properties");
        Properties appProp = new Properties();
        if (appFile.exists()) {
            try (FileInputStream fis = new FileInputStream(appFile)) {
                appProp.load(fis);
            }
        }
        return appProp;
    }

    private static Map<Long, byte[]> getPersonsPhotos(String url, Properties connProp, String driver) throws SQLException, ClassNotFoundException {
        Class.forName(driver);
        String query = "SELECT p.image, p.person_id FROM photos p";

        try(Connection connection = DriverManager.getConnection(url, connProp);
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery()) {
            Map<Long, byte[]> map = new HashMap<>(resultSet.getRow());
            while (resultSet.next()) {
                map.put(resultSet.getLong("person_id"),
                        resultSet.getBytes("image"));
            }
            return map;
        }
    }

    private static void save(Map<Long, byte[]> map) throws IOException {
        for (Map.Entry<Long, byte[]> entry: map.entrySet()) {
            Path path = Paths.get("./avatars")
                    .resolve(entry.getKey() + ".jpg");
            File file = path.toFile();
            if (!file.exists()) {
                try(ByteArrayInputStream inputStream = new ByteArrayInputStream(entry.getValue())) {
                    Files.copy(inputStream, path);
                }
            }
        }
    }

    public static void main(String[] args) throws IOException, SQLException {
        Properties properties = getProperties();
        String url = properties.getProperty("spring.datasource.url");
        String driver = properties.getProperty("spring.datasource.driver-class-name");
        String username = properties.getProperty("spring.datasource.username");
        String password = properties.getProperty("spring.datasource.password");

        Properties connProp = new Properties();
        connProp.setProperty("user", username);
        connProp.setProperty("password", password);

        save(getPersonsPhotos(url, connProp, driver));

    }


}
