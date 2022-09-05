package http;

import java.util.regex.Pattern;

public record HttpRoute(Pattern pattern, HttpHandler handler) {
    public static HttpRoute of(Pattern pattern, HttpHandler handler) {
        return new HttpRoute(pattern, handler);
    }

    public static HttpRoute of(String path, HttpHandler handler) {
        return new HttpRoute(Pattern.compile(Pattern.quote(path)), handler);
    }
}
