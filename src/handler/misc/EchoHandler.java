package handler.misc;

import http.*;

import java.util.Map;

public record EchoHandler() implements HttpHandler {
    @Override
    public boolean accept(HttpRequest request) {
        return request.path().equals("/echo");
    }

    @Override
    public HttpResponse handle(HttpRequest request) {
        return new HttpResponse(new HttpStatus(200, "OK"), new HttpHeaders(Map.of()), request.body());
    }
}
