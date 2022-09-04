package handler.permissions;

import db.Database;
import http.*;
import model.Permission;
import model.UserPermission;
import session.Session;

import java.util.Optional;

public record UserPermissionCreateHandler() implements HttpHandler {
    @Override
    public boolean accept(HttpRequest request) {
        return request.isPost() && request.path().equals("/user_permission");
    }

    @Override
    public HttpResponse handle(HttpContext context) {
        HttpRequest request = context.request();
        Optional<Session> session = context.session();
        Database db = context.database();

        if (session.isEmpty()) {
            return new HttpResponse(HttpStatus.UNAUTHORIZED);
        }

        if (!db.userPermissions.exists(new UserPermission(session.get().user(), Permission.MANAGE_PERMISSIONS))) {
            return new HttpResponse(HttpStatus.FORBIDDEN);
        }

        HttpForm form = HttpForm.from(request.body());

        if (!form.has("user", "permission")) {
            return new HttpResponse(HttpStatus.BAD_REQUEST);
        }

        db.userPermissions.insert(new UserPermission(form.get("user"), Permission.valueOf(form.get("permission"))));

        return new HttpResponse(HttpStatus.CREATED);
    }
}
