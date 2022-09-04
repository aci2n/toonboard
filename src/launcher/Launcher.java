package launcher;

import handler.*;
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
        List<HttpHandler> handlers = List.of(
                new StaticHandler(),
                new SQLiteHandler(),
                new StopHandler(cancel),
                new EchoHandler(),
                new MethodNotAllowedHandler());
        HttpServer server = new HttpServer(arguments.port, handlers);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> cancel.set(true)));
        server.start(cancel);
    }
}
