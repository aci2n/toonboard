package http;

import java.util.Map;

public record HttpHeaders(Map<String, String> map) {
    public static final HttpHeaders EMPTY = new HttpHeaders(Map.of());
}
