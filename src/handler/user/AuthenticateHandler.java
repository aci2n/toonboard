package handler.user;

import db.Database;
import http.*;
import model.User;
import session.Session;
import session.SessionManager;

public record AuthenticateHandler() implements HttpHandler {
    @Override
    public HttpResponse post(HttpContext context) {
        HttpForm form = context.request().form();
        Database db = context.database();
        SessionManager sm = context.sessionManager();

        if (!form.has("name", "password")) {
            return HttpResponse.of(HttpStatus.BAD_REQUEST);
        }

        User user = new User(form.get("name"), form.get("password"));

        if (!db.users.exists(user)) {
            return HttpResponse.of(HttpStatus.UNAUTHORIZED);
        }

        Session session = sm.newSession(user.name());

        return HttpResponse.builder().cookie(Session.COOKIE_KEY, session.key().uuid().toString()).build();
    }
}
