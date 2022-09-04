package handler.user;

import db.Database;
import http.*;
import model.User;

public record CreateUserHandler(Database db) implements HttpHandler {
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

        db.users().insert(new User(form.get("name"), form.get("password")));

        return new HttpResponse(HttpStatus.CREATED);
    }
}
