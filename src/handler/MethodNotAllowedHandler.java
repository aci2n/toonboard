package handler;

import http.*;

import java.nio.ByteBuffer;

public record MethodNotAllowedHandler() implements HttpHandler {
    @Override
    public boolean accept(HttpRequest request) {
        return true;
    }

    @Override
    public HttpResponse handle(HttpRequest request) {
        return new HttpResponse(HttpStatus.METHOD_NOT_ALLOWED);
    }
}
