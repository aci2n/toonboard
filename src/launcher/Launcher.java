package launcher;

import db.Database;
import handler.admin.StopHandler;
import handler.misc.StaticHandler;
import handler.permissions.UserPermissionHandler;
import handler.user.AuthenticateHandler;
import handler.user.LoginHandler;
import handler.user.UserHandler;
import http.HttpRoute;
import http.HttpServer;
import session.SessionManager;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;

public record Launcher() {
    private record Arguments(int port) {
        private static Arguments from(String[] args) {
            int port = 80;

            if (args.length >= 2) {
                try {
                    port = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    // ignored
                }
            }

            return new Arguments(port);
        }
    }

    public static void main(String[] args) throws IOException {
        Arguments arguments = Arguments.from(args);
        AtomicBoolean cancel = new AtomicBoolean(false);
        Database db = new Database("toonboard.db");
        List<HttpRoute> handlers = List.of(
                HttpRoute.of("/stop", new StopHandler(cancel)),
                HttpRoute.of("/user", new UserHandler()),
                HttpRoute.of("/user_permission", new UserPermissionHandler()),
                HttpRoute.of("/auth", new AuthenticateHandler()),
                HttpRoute.of("/login", new LoginHandler()),
                HttpRoute.of(Pattern.compile("/(.*)"), new StaticHandler()));
        HttpServer server = new HttpServer(arguments.port, handlers, db, new SessionManager());

        db.setup();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> cancel.set(true)));
        server.start(cancel);
    }
}
