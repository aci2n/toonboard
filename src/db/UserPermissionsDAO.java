package db;

import model.UserPermission;

import java.sql.PreparedStatement;
import java.sql.Statement;

public class UserPermissionsDAO {
    private final Database db;

    UserPermissionsDAO(Database db) {
        this.db = db;
    }

    void createTable() {
        db.withConnection(connection -> {
            Statement statement = connection.createStatement();
            statement.executeUpdate("""
                    create table if not exists user_permissions (
                        user integer,
                        permission text,
                        primary key(user, permission),
                        foreign key(user) references users(name) on delete cascade on update cascade
                    )""");
            return null;
        });
    }

    public void insert(UserPermission userPermission) {
        db.withConnection(connection -> {
            PreparedStatement stmt = connection.prepareStatement("""
                    insert or ignore into user_permissions (user, permission)
                    values (?, ?)""");
            stmt.setString(1, userPermission.user());
            stmt.setString(2, userPermission.permission().toString());
            stmt.executeUpdate();
            return null;
        });
    }

    public void delete(UserPermission userPermission) {
        db.withConnection(connection -> {
            PreparedStatement stmt = connection.prepareStatement("""
                    delete from user_permissions
                    where user = ? and permission = ?""");
            stmt.setString(1, userPermission.user());
            stmt.setString(2, userPermission.permission().toString());
            stmt.executeUpdate();
            return null;
        });
    }

    public boolean exists(UserPermission userPermission) {
        return db.selectOne(connection -> {
            PreparedStatement stmt = connection.prepareStatement("""
                    select count(*) as count
                    from users
                    where user = ? and permission = ?""");
            stmt.setString(1, userPermission.user());
            stmt.setString(2, userPermission.permission().toString());
            return stmt;
        }, rs -> rs.getInt("count")).orElseThrow() == 1;
    }
};
