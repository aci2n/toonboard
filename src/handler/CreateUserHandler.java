package handler;

import db.Database;
import http.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public record CreateUserHandler(Database db) implements HttpHandler {
    public CreateUserHandler {
        db.withConnection(connection -> {
            Statement statement = connection.createStatement();
            statement.executeUpdate("""
                    create table if not exists (
                        id int primary key,
                        name text,
                         
                    )
                    """);
            return null;
        });
    }

    @Override
    public boolean accept(HttpRequest request) {
        return request.isPost() && request.path().equals("/user");
    }

    @Override
    public HttpResponse handle(HttpRequest request) {
        return db.withConnection(connection -> {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate();
            return new HttpResponse(HttpStatus.OK);
        });
    }
}
