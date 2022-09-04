package db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface ResultSetDeserializer<T> {
    T deserialize(ResultSet rs) throws SQLException;
}
