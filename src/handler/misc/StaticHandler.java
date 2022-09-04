package handler.misc;

import http.*;
import util.Resource;

import java.io.IOException;

public record StaticHandler() implements HttpHandler {

    public static final String PREFIX = "/static";

    @Override
    public boolean accept(HttpRequest request) {
        return request.path().startsWith(PREFIX);
    }

    @Override
    public HttpResponse handle(HttpRequest request) {
        try {
            return new HttpResponse(HttpStatus.OK, HttpHeaders.EMPTY, Resource.getResource(request.path()));
        } catch (IOException e) {
            return new HttpResponse(HttpStatus.NOT_FOUND);
        }
    }
}
