package db;

import model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

public class UserDAO {
    private final Database db;

    UserDAO(Database db) {
        this.db = db;
    }

    public void createTable() {
        db.withConnection(connection -> {
            Statement statement = connection.createStatement();
            statement.executeUpdate("""
                    create table if not exists users (
                        name text primary key,
                        password blob
                    )""");
            return null;
        });
    }

    public void insert(User user) {
        db.withConnection(connection -> {
            PreparedStatement stmt = connection.prepareStatement("""
                    insert into users (name, password)
                    values (?, ?)""");
            stmt.setString(1, user.name());
            stmt.setBytes(2, user.password());
            stmt.executeUpdate();
            return null;
        });
    }

    public void update(User user) {
        db.withConnection(connection -> {
            PreparedStatement stmt = connection.prepareStatement("""
                    update users
                    set password = ?
                    where name = ?""");
            stmt.setBytes(1, user.password());
            stmt.setString(2, user.name());
            stmt.executeUpdate();
            return null;
        });
    }

    public void delete(User user) {
        db.withConnection(connection -> {
            PreparedStatement stmt = connection.prepareStatement("""
                    delete from users
                    where name = ?""");
            stmt.setString(1, user.name());
            stmt.executeUpdate();
            return null;
        });
    }

    public boolean exists(User user) {
        return db.selectOne(connection -> {
            PreparedStatement stmt = connection.prepareStatement("""
                    select count(*) as count
                    from users
                    where name = ? and password = ?""");
            stmt.setString(1, user.name());
            stmt.setBytes(2, user.password());
            return stmt;
        }, rs -> rs.getInt("count")).orElseThrow() == 1;
    }

    public Optional<User> byName(String name) {
        return db.selectOne(connection -> {
            PreparedStatement stmt = connection.prepareStatement("""
                    select name, password
                    from users
                    where name = ?""");
            stmt.setString(1, name);
            return stmt;
        }, this::deserialize);
    }

    public List<User> all() {
        return db.selectMany(connection -> connection.prepareStatement("""
                select name, password
                from users"""
        ), this::deserialize);
    }

    public User deserialize(ResultSet rs) throws SQLException {
        return new User(rs.getString("name"), rs.getBytes("password"));
    }
};
