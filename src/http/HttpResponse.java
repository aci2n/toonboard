package http;

import java.nio.ByteBuffer;

public record HttpResponse(HttpStatus status, HttpHeaders headers, ByteBuffer body) {

    public HttpResponse(HttpStatus status) {
        this(status, HttpHeaders.EMPTY, ByteBuffer.allocate(0));
    }
}
