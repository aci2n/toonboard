package handler.permissions;

import Session.Session;
import Session.SessionManager;
import db.Database;
import http.*;
import model.Permission;
import model.UserPermission;

import java.util.Optional;

public record UserPermissionCreateHandler(Database db, SessionManager sm) implements HttpHandler {
    @Override
    public boolean accept(HttpRequest request) {
        return request.isPost() && request.path().equals("/user_permission");
    }

    @Override
    public HttpResponse handle(HttpRequest request) {
        Optional<Session> session = sm.getSession(request);

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
