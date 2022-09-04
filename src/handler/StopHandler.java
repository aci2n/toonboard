package handler;

import http.*;

import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicBoolean;

public record StopHandler(AtomicBoolean cancel) implements HttpHandler {
    @Override
    public boolean accept(HttpRequest request) {
        return request.path().equals("/stop");
    }

    @Override
    public HttpResponse handle(HttpRequest request) {
        cancel.set(true);
        return new HttpResponse(HttpStatus.OK, HttpHeaders.EMPTY, ByteBuffer.allocate(0));
    }
}
