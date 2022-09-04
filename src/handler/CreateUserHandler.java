package handler;

import db.Database;
import http.*;
import util.Crypto;

import java.sql.PreparedStatement;
import java.sql.Statement;

public record CreateUserHandler(Database db) implements HttpHandler {
    public CreateUserHandler {
        db.withConnection(connection -> {
            Statement statement = connection.createStatement();
            statement.executeUpdate("""
                    create table if not exists users (
                        id int primary key,
                        name text unique,
                        password blob
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
        HttpForm form = HttpForm.from(request.body());

        if (!form.has("name") || !form.has("password")) {
            return new HttpResponse(HttpStatus.BAD_REQUEST);
        }

        return db.withConnection(connection -> {
            PreparedStatement stmt = connection.prepareStatement("""
                    insert into users (name, password)
                    values (?, ?)
                    """);

            stmt.setString(1, form.get("name"));
            stmt.setBytes(2, Crypto.sha1(form.get("password")));
            stmt.executeUpdate();

            return new HttpResponse(HttpStatus.OK);
        });
    }
}
