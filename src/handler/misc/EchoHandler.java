package handler.misc;

import http.*;

public record EchoHandler() implements HttpHandler {
    @Override
    public HttpResponse get(HttpContext context) {
        return HttpResponse.builder().body(context.request().body()).build();
    }
}
