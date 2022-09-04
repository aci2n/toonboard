package http;

public interface HttpHandler {
    boolean accept(HttpRequest request);

    HttpResponse handle(HttpRequest request);
}
