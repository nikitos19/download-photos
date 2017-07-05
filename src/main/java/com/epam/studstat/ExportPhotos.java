package com.epam.studstat;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

class ExportPhotos {

    void savePhotos(Map<Long, byte[]> map) throws IOException {
        for (Map.Entry<Long, byte[]> entry : map.entrySet()) {
            File jarPath = new File(App.class.getProtectionDomain().getCodeSource().getLocation().getPath());
            String propertiesPath = jarPath.getParentFile().getAbsolutePath() + "/avatars";
            File file = new File(propertiesPath);

            Path path = Paths.get(file.getPath())
                    .resolve(entry.getKey() + ".jpg");

            if (file.exists()) {
                try (ByteArrayInputStream inputStream = new ByteArrayInputStream(entry.getValue())) {
                    Files.copy(inputStream, path);
                }
            }
        }
    }

    Map<Long, byte[]> getPersonsPhotos(String url, Properties connProp, String driver) throws SQLException, ClassNotFoundException, IOException {
        Class.forName(driver);
        String query = "SELECT p.image, p.person_id FROM photo p";

        try (Connection connection = DriverManager.getConnection(url, connProp);
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            Map<Long, byte[]> photosMap = new HashMap<>(resultSet.getRow());
            while (resultSet.next()) {
                photosMap.put(resultSet.getLong("person_id"),
                        resultSet.getBytes("image"));
            }
            return photosMap;
        }
    }
}
