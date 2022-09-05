package http;

public interface HttpHandler {
    static HttpResponse notAllowed() {
        return HttpResponse.of(HttpStatus.METHOD_NOT_ALLOWED);
    }

    default HttpResponse get(HttpContext context) {
        return notAllowed();
    }

    default HttpResponse post(HttpContext context) {
        return notAllowed();
    }

    default HttpResponse put(HttpContext context) {
        return notAllowed();
    }

    default HttpResponse patch(HttpContext context) {
        return notAllowed();
    }

    default HttpResponse head(HttpContext context) {
        return notAllowed();
    }

    default HttpResponse delete(HttpContext context) {
        return notAllowed();
    }

    default HttpResponse connect(HttpContext context) {
        return notAllowed();
    }
}
