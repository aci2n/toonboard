package handler;

import http.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public record StaticHandler() implements HttpHandler {

    public static final String PREFIX = "/static";

    @Override
    public boolean accept(HttpRequest request) {
        return request.path().startsWith(PREFIX);
    }

    @Override
    public HttpResponse handle(HttpRequest request) {
        String file = request.path().substring(PREFIX.length());

        if (file.startsWith("/")) {
            file = file.substring(1);
        }
        if (file.isEmpty()) {
            file = "home.html";
        }
        if (!file.contains(".")) {
            file += ".html";
        }

        final byte[] body;

        try (InputStream resource = getClass().getResourceAsStream(file)) {
            if (resource == null) {
                return new HttpResponse(HttpStatus.NOT_FOUND);
            }
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            resource.transferTo(out);
            body = out.toByteArray();
        } catch (IOException e) {
            return new HttpResponse(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new HttpResponse(HttpStatus.OK, HttpHeaders.EMPTY, body);
    }
}
