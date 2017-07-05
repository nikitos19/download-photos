package com.epam.studstat;

import javafx.util.Pair;

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

    private static final String SELECT_PHOTOS_SQL = "SELECT p.image, p.person_id, p.format FROM photo p";

    void savePhotos(String avatarsDir, Map<Long, Pair<byte[], String>> map) throws IOException {
        for (Map.Entry<Long, Pair<byte[], String>> entry : map.entrySet()) {
            File dir = new File(avatarsDir);

            Pair<byte[], String> imageAndFormat = entry.getValue();
            String[] imageFormat = imageAndFormat.getValue().split("[/]");
            Path path = Paths.get(dir.getPath())
                    .resolve(entry.getKey() + "." + imageFormat[imageFormat.length-1]);

            if (dir.exists()) {
                try (ByteArrayInputStream inputStream = new ByteArrayInputStream(imageAndFormat.getKey())) {
                    Files.copy(inputStream, path);
                }
            }
        }
    }

    Map<Long, Pair<byte[], String>> getPersonsPhotos(String url, Properties connProp, String driver) throws SQLException, ClassNotFoundException, IOException {
        Class.forName(driver);
        try (Connection connection = DriverManager.getConnection(url, connProp);
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_PHOTOS_SQL);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            Map<Long, Pair<byte[], String>> photosMap = new HashMap<>(resultSet.getRow());
            while (resultSet.next()) {
                Pair<byte[], String> imageAndFormat = new Pair<>(
                        resultSet.getBytes("image"),
                        resultSet.getString("format"));
                photosMap.put(resultSet.getLong("person_id"),
                        imageAndFormat);
            }
            return photosMap;
        }
    }
}
