package handler.user;

import db.Database;
import http.*;
import model.User;
import session.Session;
import session.SessionManager;

public record AuthenticateHandler() implements HttpHandler {
    @Override
    public boolean accept(HttpRequest request) {
        return request.isPost() && request.path().equals("/auth");
    }

    @Override
    public HttpResponse handle(HttpContext context) {
        HttpForm form = context.form();
        Database db = context.database();
        SessionManager sm = context.sessionManager();

        if (!form.has("name", "password")) {
            return new HttpResponse(HttpStatus.BAD_REQUEST);
        }

        User user = new User(form.get("name"), form.get("password"));

        if (!db.users.exists(user)) {
            return new HttpResponse(HttpStatus.UNAUTHORIZED);
        }

        HttpResponse response = new HttpResponse(HttpStatus.OK);
        Session session = sm.newSession(user.name());

        response.cookies().put("session", session.key().uuid().toString());

        return response;
    }
}
