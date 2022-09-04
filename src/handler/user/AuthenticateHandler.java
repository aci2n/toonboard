package handler.user;

import Session.SessionManager;
import db.Database;
import http.*;
import model.User;

public record AuthenticateHandler(Database db, SessionManager sm) implements HttpHandler {
    @Override
    public boolean accept(HttpRequest request) {
        return request.isPost() && request.path().equals("/auth");
    }

    @Override
    public HttpResponse handle(HttpRequest request) {
        HttpForm form = HttpForm.from(request.body());

        if (!form.has("name", "password")) {
            return new HttpResponse(HttpStatus.BAD_REQUEST);
        }

        User user = new User(form.get("name"), form.get("password"));

        if (!db.users.exists(user)) {
            return new HttpResponse(HttpStatus.UNAUTHORIZED);
        }

        HttpResponse response = new HttpResponse(HttpStatus.OK);
        sm.newSession(response, user.name());

        return response;
    }
}
