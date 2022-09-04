package http;

public record HttpResponse(HttpStatus status, HttpHeaders headers, byte[] body) {

    public HttpResponse(HttpStatus status) {
        this(status, HttpHeaders.EMPTY, new byte[0]);
    }
}
