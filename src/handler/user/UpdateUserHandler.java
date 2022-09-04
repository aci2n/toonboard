package handler.user;

import db.Database;
import http.*;
import model.User;

public record UpdateUserHandler() implements HttpHandler {
    @Override
    public boolean accept(HttpRequest request) {
        return request.isPatch() && request.path().equals("/user");
    }

    @Override
    public HttpResponse handle(HttpContext context) {
        HttpForm form = context.form();
        Database db = context.database();

        if (!form.has("name") || !form.has("password")) {
            return new HttpResponse(HttpStatus.BAD_REQUEST);
        }

        db.users.update(new User(form.get("name"), form.get("password")));

        return new HttpResponse(HttpStatus.OK);
    }
}
