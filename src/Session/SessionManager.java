package session;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public record SessionManager(ConcurrentMap<SessionKey, Session> sessions) {
    public SessionManager() {
        this(new ConcurrentHashMap<>());
    }

    public Session newSession(String user) {
        Session session = new Session(new SessionKey(UUID.randomUUID()), user);
        sessions.put(session.key(), session);
        return session;
    }

    public Optional<Session> getSession(String cookie) {
        final UUID uuid;
        try {
            uuid = UUID.fromString(cookie);
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
        return Optional.ofNullable(sessions.get(new SessionKey(uuid)));
    }
}
