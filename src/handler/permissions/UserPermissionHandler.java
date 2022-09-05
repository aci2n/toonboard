package handler.permissions;

import db.Database;
import http.*;
import model.Permission;
import model.UserPermission;
import session.Session;

import java.util.Optional;

public record UserPermissionHandler() implements HttpHandler {
    @Override
    public HttpResponse post(HttpContext context) {
        Optional<Session> session = context.session();
        Database db = context.database();

        if (session.isEmpty()) {
            return HttpResponse.of(HttpStatus.UNAUTHORIZED);
        }

        if (!db.userPermissions.exists(new UserPermission(session.get().user(), Permission.MANAGE_PERMISSIONS))) {
            return HttpResponse.of(HttpStatus.FORBIDDEN);
        }

        HttpForm form = context.request().form();

        if (!form.has("user", "permission")) {
            return HttpResponse.of(HttpStatus.BAD_REQUEST);
        }

        db.userPermissions.insert(new UserPermission(form.get("user"), Permission.valueOf(form.get("permission"))));

        return HttpResponse.of(HttpStatus.CREATED);
    }
}
