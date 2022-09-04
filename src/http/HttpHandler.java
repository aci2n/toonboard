package http;

public interface HttpHandler {
    @Deprecated
    default boolean accept(HttpRequest request) {
        return false;
    }

    @Deprecated
    default HttpResponse handle(HttpRequest request) {
        return new HttpResponse(HttpStatus.METHOD_NOT_ALLOWED);
    }

    default boolean accept(HttpContext context) {
        return accept(context.request());
    }

    default HttpResponse handle(HttpContext context) {
        return handle(context.request());
    }
}
