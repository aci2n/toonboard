package handler.admin;

import http.HttpHandler;
import http.HttpRequest;
import http.HttpResponse;
import http.HttpStatus;

import java.util.concurrent.atomic.AtomicBoolean;

public record StopHandler(AtomicBoolean cancel) implements HttpHandler {
    @Override
    public boolean accept(HttpRequest request) {
        return request.path().equals("/stop");
    }

    @Override
    public HttpResponse handle(HttpRequest request) {
        cancel.set(true);
        return new HttpResponse(HttpStatus.OK);
    }
}
