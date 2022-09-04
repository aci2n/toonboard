package db;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public final class Database {
    private final String url;
    private final UserDAO users;

    public Database(String url) {
        this.url = url;
        this.users = new UserDAO(this);
    }

    public void setup() {
        users.createTable();
    }

    public UserDAO users() {
        return users;
    }

    public <T> T withConnection(ConnectionHandler<T> handler) {
        try (Connection connection = DriverManager.getConnection(url)) {
            return handler.handle(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    <T> Optional<T> selectOne(ConnectionHandler<PreparedStatement> handler, ResultSetDeserializer<T> deserializer) {
        return withConnection(connection -> {
            PreparedStatement stmt = handler.handle(connection);
            if (!stmt.execute()) {
                return Optional.empty();
            }
            ResultSet rs = stmt.getResultSet();
            if (!rs.next()) {
                return Optional.empty();
            }
            return Optional.of(deserializer.deserialize(rs));
        });
    }

    <T> List<T> selectMany(ConnectionHandler<PreparedStatement> handler, ResultSetDeserializer<T> deserializer) {
        return withConnection(connection -> {
            PreparedStatement stmt = handler.handle(connection);
            if (!stmt.execute()) {
                return List.of();
            }
            ResultSet rs = stmt.getResultSet();
            List<T> list = new ArrayList<>();
            while (rs.next()) {
                list.add(deserializer.deserialize(rs));
            }
            return Collections.unmodifiableList(list);
        });
    }
}
