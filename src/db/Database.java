package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public record Database(String url) {
    public <T> T withConnection(ConnectionHandler<T> handler) {
        try (Connection connection = DriverManager.getConnection(url)) {
            return handler.handle(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
