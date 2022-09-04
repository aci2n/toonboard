package handler.misc;

import http.*;

public record EchoHandler() implements HttpHandler {
    @Override
    public boolean accept(HttpRequest request) {
        return request.path().equals("/echo");
    }

    @Override
    public HttpResponse handle(HttpRequest request) {
        return new HttpResponse(HttpStatus.OK, HttpHeaders.EMPTY, request.body());
    }
}
