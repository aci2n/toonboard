package handler.user;

import db.Database;
import http.*;
import model.User;

public record UserHandler() implements HttpHandler {
    @Override
    public HttpResponse post(HttpContext context) {
        Database db = context.database();
        HttpForm form = context.request().form();

        if (!form.has("name") || !form.has("password")) {
            return HttpResponse.of(HttpStatus.BAD_REQUEST);
        }

        db.users.insert(new User(form.get("name"), form.get("password")));

        return HttpResponse.of(HttpStatus.CREATED);
    }

    @Override
    public HttpResponse delete(HttpContext context) {
        HttpForm form = context.request().form();
        Database db = context.database();

        if (!form.has("name")) {
            return HttpResponse.of(HttpStatus.BAD_REQUEST);
        }

        db.users.delete(new User(form.get("name")));

        return HttpResponse.of(HttpStatus.OK);
    }

    @Override
    public HttpResponse put(HttpContext context) {
        HttpForm form = context.request().form();
        Database db = context.database();

        if (!form.has("name") || !form.has("password")) {
            return HttpResponse.of(HttpStatus.BAD_REQUEST);
        }

        db.users.update(new User(form.get("name"), form.get("password")));

        return HttpResponse.of(HttpStatus.OK);
    }
}
