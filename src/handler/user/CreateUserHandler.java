package handler.user;

import db.Database;
import http.*;
import model.User;

public record CreateUserHandler() implements HttpHandler {
    @Override
    public boolean accept(HttpRequest request) {
        return request.isPost() && request.path().equals("/user");
    }

    @Override
    public HttpResponse handle(HttpContext context) {
        Database db = context.database();
        HttpForm form = context.form();

        if (!form.has("name") || !form.has("password")) {
            return new HttpResponse(HttpStatus.BAD_REQUEST);
        }

        db.users.insert(new User(form.get("name"), form.get("password")));

        return new HttpResponse(HttpStatus.CREATED);
    }
}
