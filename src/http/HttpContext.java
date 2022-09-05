package http;

import db.Database;
import session.Session;
import session.SessionManager;

import java.util.Optional;

public record HttpContext(
        HttpRequest request,
        Database database,
        SessionManager sessionManager,
        MatchedRoute matchedRoute) {
    public Optional<Session> session() {
        return request.cookies().get(Session.COOKIE_KEY).flatMap(sessionManager::getSession);
    }
}
