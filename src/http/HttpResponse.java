package http;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class HttpResponse {
    private final HttpStatus status;
    private final HttpHeaders headers;
    private final byte[] body;
    private final Map<String, String> cookies;

    public HttpResponse(HttpStatus status, HttpHeaders headers, byte[] body) {
        this.status = status;
        this.headers = headers;
        this.body = body;
        this.cookies = new HashMap<>();
    }

    public HttpResponse(HttpStatus status) {
        this(status, HttpHeaders.EMPTY, new byte[0]);
    }

    public HttpResponse(HttpStatus status, HttpHeaders headers, String body) {
        this(status, headers, body.getBytes(StandardCharsets.UTF_8));
    }

    public HttpResponse(HttpStatus status, HttpHeaders headers, Exception e) {
        this(status, headers, e.getMessage());
    }

    public HttpStatus status() {
        return status;
    }

    public HttpHeaders headers() {
        return headers;
    }

    public byte[] body() {
        return body;
    }

    public Map<String, String> cookies() {
        return cookies;
    }
}
