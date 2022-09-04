package handler.user;

import http.*;
import util.Resource;

import java.io.IOException;

public class LoginPageHandler implements HttpHandler {
    @Override
    public boolean accept(HttpContext context) {
        return context.request().isGet() && context.request().path().equals("/login");
    }

    @Override
    public HttpResponse handle(HttpContext context) {
        try {
            return new HttpResponse(HttpStatus.OK, HttpHeaders.EMPTY, Resource.getResource("/static/login.html"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
