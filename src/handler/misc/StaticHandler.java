package handler.misc;

import http.HttpContext;
import http.HttpHandler;
import http.HttpResponse;
import util.Resource;

public record StaticHandler() implements HttpHandler {
    @Override
    public HttpResponse get(HttpContext context) {
        return HttpResponse.builder().body(Resource.getResource(context.request().path())).build();
    }
}
