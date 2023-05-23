package screbber.DAO;

import screbber.Definition;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class DefinitionDAO {
    public void insertDefinition(Definition definition) {
        String sql = "INSERT INTO definition (name, info, category_id) VALUES (?, ?, ?)";

        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, definition.getName());
            statement.setString(2, definition.getInfo());
            statement.setInt(3, definition.getCategory());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertDefinitionList(List<Definition> definitionList) {
        for (Definition definition : definitionList) {
            insertDefinition(definition);
        }
    }
}
