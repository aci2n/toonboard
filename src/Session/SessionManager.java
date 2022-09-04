package Session;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public record SessionManager(ConcurrentMap<SessionKey, Session> sessions) {
    public SessionManager() {
        this(new ConcurrentHashMap<>());
    }

    public Session newSession() {
        Session session = new Session(new SessionKey(UUID.randomUUID()));
        sessions.put(session.key(), session);
        return session;
    }
}
