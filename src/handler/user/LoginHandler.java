package handler.user;

import http.HttpContext;
import http.HttpHandler;
import http.HttpResponse;
import util.Resource;

public class LoginHandler implements HttpHandler {
    @Override
    public HttpResponse get(HttpContext context) {
        return HttpResponse.builder().body(Resource.getResource("/login.html")).build();
    }
}
