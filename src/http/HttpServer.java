package http;

import util.Logging;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public record HttpServer(int port, List<HttpHandler> handlers) {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH).withZone(ZoneId.of("GMT"));
    private static final Logger LOGGER = Logging.get(HttpServer.class);

    public void start(AtomicBoolean cancel) throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(port, 50, InetAddress.getByName("0.0.0.0"))) {
            while (!cancel.get()) {
                try (Socket clientSocket = serverSocket.accept()) {
                    LOGGER.fine(() -> String.format("HTTP request [remote=%s]", clientSocket.getInetAddress()));

                    clientSocket.setSoTimeout((int) Duration.ofSeconds(5).toMillis());

                    HttpResponse response = handleRequest(clientSocket.getInputStream());
                    PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());

                    writer.printf("HTTP/1.1 %d %s%n", response.status().code(), response.status().description());

                    Map<String, String> headers = new HashMap<>(response.headers().map());
                    headers.put("server", "i2n");
                    headers.put("date", DATE_FORMATTER.format(Instant.now()));
                    headers.put("content-length", Integer.toString(response.body().length));
                    headers.put("cookie", response.getCookies().entrySet().stream()
                            .map(cookie -> String.format("%s=%s", cookie.getKey(), cookie.getValue()))
                            .collect(Collectors.joining("; ")));
                    headers.putIfAbsent("content-type", "text/html; charset=UTF8");
                    headers.forEach((key, value) -> writer.printf("%s: %s%n", key, value));

                    writer.printf("%n");
                    writer.flush();

                    clientSocket.getOutputStream().write(response.body());
                    clientSocket.getOutputStream().flush();
                } catch (IOException e) {
                    LOGGER.log(Level.WARNING, "HTTP request failed", e);
                }
            }
        }
    }

    private HttpResponse handleRequest(InputStream inputStream) {
        final HttpRequest request;

        try {
            request = HttpRequest.readRequest(inputStream);
        } catch (IOException e) {
            return new HttpResponse(HttpStatus.BAD_REQUEST);
        }

        Optional<HttpHandler> handler = handlers.stream().filter(h -> h.accept(request)).findFirst();

        if (handler.isEmpty()) {
            throw new RuntimeException("no handlers found for request");
        }

        try {
            return handler.get().handle(request);
        } catch (Exception e) {
            return new HttpResponse(HttpStatus.INTERNAL_SERVER_ERROR, HttpHeaders.EMPTY, e);
        }
    }
}
