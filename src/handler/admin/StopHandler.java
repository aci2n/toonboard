package handler.admin;

import http.HttpContext;
import http.HttpHandler;
import http.HttpResponse;
import http.HttpStatus;

import java.util.concurrent.atomic.AtomicBoolean;

public record StopHandler(AtomicBoolean cancel) implements HttpHandler {
    @Override
    public HttpResponse post(HttpContext context) {
        cancel.set(true);
        return HttpResponse.builder().build();
    }
}
