package handler.user;

import Session.Session;
import Session.SessionManager;
import db.Database;
import http.*;
import model.User;

public record AuthenticateHandler(Database db, SessionManager sessionManager) implements HttpHandler {
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

        if (!db.users().exists(new User(form.get("name"), form.get("password")))) {
            return new HttpResponse(HttpStatus.UNAUTHORIZED);
        }

        HttpResponse response = new HttpResponse(HttpStatus.OK);
        Session session = sessionManager.newSession();

        response.addCookie("session", session.key().uuid().toString());

        return response;
    }
}
