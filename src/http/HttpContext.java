package http;

import db.Database;
import session.Session;
import session.SessionManager;

import java.util.Optional;

public record HttpContext(HttpRequest request, Database database, SessionManager sessionManager) {
    public HttpForm form() {
        return HttpForm.from(request.body());
    }

    public Optional<Session> session() {
        return sessionManager.getSession(request);
    }
}
