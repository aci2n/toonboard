package handler.user;

import db.Database;
import http.*;
import model.User;

public record DeleteUserHandler(Database db) implements HttpHandler {
    @Override
    public boolean accept(HttpRequest request) {
        return request.isDelete() && request.path().equals("/user");
    }

    @Override
    public HttpResponse handle(HttpRequest request) {
        HttpForm form = HttpForm.from(request.body());

        if (!form.has("name")) {
            return new HttpResponse(HttpStatus.BAD_REQUEST);
        }

        db.users.delete(new User(form.get("name")));

        return new HttpResponse(HttpStatus.OK);
    }
}
