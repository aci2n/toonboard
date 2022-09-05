package session;

public record Session(SessionKey key, String user) {
    public static final String COOKIE_KEY = "session";
}
