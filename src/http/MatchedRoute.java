package http;

import java.util.regex.Matcher;

public record MatchedRoute(HttpRoute route, Matcher matcher) {
}
