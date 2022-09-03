package http;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public record HttpServer(int port) {
    public void loop() throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);

        while (true) {
            try (Socket clientSocket = serverSocket.accept()) {
                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
                HttpRequest request = HttpRequest.readRequest(clientSocket.getInputStream());

                System.out.println(request);

                writer.printf("%s 200 OK%n", request.version());
                writer.printf("Content-Length: %d%n", request.body().array().length);
                writer.printf("%n");
                writer.printf("%s", new String(request.body().array(), StandardCharsets.UTF_8));
                writer.flush();
            }
        }
    }
}
