package launcher;

import http.HttpServer;
import util.IntegerReader;

import java.io.IOException;

public record Launcher() {
    private record Arguments(int port) {
        private Arguments(String[] args) {
            this(readPort(args.length >= 2 ? args[1] : null));
        }

        private static int readPort(String port) {
            return IntegerReader.readOrDefault(port, 80);
        }
    }

    public static void main(String[] args) throws IOException {
        Arguments arguments = new Arguments(args);
        new HttpServer(arguments.port()).loop();
    }
}
