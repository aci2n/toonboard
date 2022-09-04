package launcher;

import Session.SessionManager;
import db.Database;
import handler.misc.*;
import handler.user.AuthenticateHandler;
import handler.user.CreateUserHandler;
import handler.user.DeleteUserHandler;
import handler.user.UpdateUserHandler;
import http.HttpHandler;
import http.HttpServer;

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
        Database db = new Database("jdbc:sqlite:toonboard.db");
        SessionManager sessionManager = new SessionManager();
        List<HttpHandler> handlers = List.of(
                new StaticHandler(),
                new SQLiteHandler(),
                new StopHandler(cancel),
                new EchoHandler(),
                new CreateUserHandler(db),
                new DeleteUserHandler(db),
                new UpdateUserHandler(db),
                new AuthenticateHandler(db, sessionManager),
                new MethodNotAllowedHandler());
        HttpServer server = new HttpServer(arguments.port, handlers);

        db.setup();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> cancel.set(true)));
        server.start(cancel);
    }
}
