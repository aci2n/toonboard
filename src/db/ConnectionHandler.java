package db;

import java.sql.Connection;
import java.sql.SQLException;

@FunctionalInterface
public interface ConnectionHandler<T> {
    T handle(Connection connection) throws SQLException;
}
