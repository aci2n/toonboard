package http;

import java.nio.charset.StandardCharsets;

public record HttpResponse(HttpStatus status, HttpHeaders headers, HttpCookies cookies, byte[] body) {
    public static HttpResponse of(HttpStatus status) {
        return HttpResponse.builder().status(status).build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private HttpStatus status;
        private final HttpHeaders headers;
        private final HttpCookies cookies;
        private byte[] body;

        private Builder() {
            status = HttpStatus.OK;
            headers = new HttpHeaders();
            cookies = new HttpCookies();
            body = new byte[0];
        }

        public Builder status(HttpStatus status) {
            this.status = status;
            return this;
        }

        public Builder header(String key, String value) {
            headers.put(key, value);
            return this;
        }

        public Builder cookie(String key, String value) {
            cookies.put(key, value);
            return this;
        }

        public Builder body(String body) {
            return body(body.getBytes(StandardCharsets.UTF_8));
        }

        public Builder body(byte[] body) {
            this.body = body;
            return this;
        }

        public Builder body(Throwable throwable) {
            return body(throwable.getMessage());
        }

        public HttpResponse build() {
            return new HttpResponse(status, headers, cookies, body);
        }
    }
}
