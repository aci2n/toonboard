package http;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public record HttpCookies(Map<String, String> entries) {
    public HttpCookies() {
        this(new HashMap<>());
    }

    public void put(String key, String value) {
        entries.put(key, value);
    }

    public Optional<String> get(String key) {
        return Optional.ofNullable(entries.get(key));
    }

    @Override
    public Map<String, String> entries() {
        return Collections.unmodifiableMap(entries);
    }
}
