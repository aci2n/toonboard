package http;

public class HttpException extends RuntimeException {
    private final HttpResponse response;

    public HttpException(HttpResponse response) {
        this.response = response;
    }

    public HttpException(Throwable cause) {
        this(HttpStatus.INTERNAL_SERVER_ERROR, cause);
    }

    public HttpException(HttpStatus status, Throwable cause) {
        this(HttpResponse.builder().status(status).body(cause).build());
    }

    public HttpException(HttpStatus status) {
        this(HttpResponse.builder().status(status).build());
    }

    public HttpResponse response() {
        return response;
    }
}
