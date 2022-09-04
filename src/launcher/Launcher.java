package launcher;

import db.Database;
import handler.admin.StopHandler;
import handler.misc.EchoHandler;
import handler.misc.MethodNotAllowedHandler;
import handler.misc.StaticHandler;
import handler.permissions.UserPermissionCreateHandler;
import handler.user.*;
import http.HttpHandler;
import http.HttpServer;
import session.SessionManager;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

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
        SessionManager sm = new SessionManager();
        List<HttpHandler> handlers = List.of(
                new StaticHandler(),
                new StopHandler(cancel),
                new EchoHandler(),
                new CreateUserHandler(),
                new DeleteUserHandler(),
                new UpdateUserHandler(),
                new AuthenticateHandler(),
                new UserPermissionCreateHandler(),
                new LoginPageHandler(),
                new MethodNotAllowedHandler());
        HttpServer server = new HttpServer(arguments.port, handlers, db, new SessionManager());

        db.setup();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> cancel.set(true)));
        server.start(cancel);
    }
}
