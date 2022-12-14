package http;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public record HttpRequest(StartLine startLine, HttpHeaders headers, HttpCookies cookies, byte[] body) {
    private final static Pattern START_LINE_PATTERN = Pattern.compile("([^\s]+) ([^\s]+) ([^\s]+)");
    private final static Pattern HEADER_PATTERN = Pattern.compile("([^:]+): (.+)");
    private final static Pattern COOKIE_PATTERN = Pattern.compile("([^=]+)=([^;]+);?\s*");

    public record StartLine(HttpMethod method, String path, String version) {
    }

    static HttpRequest readRequest(InputStream inputStream) throws IOException {
        StringBuilder currentLine = new StringBuilder();
        byte[] buffer = new byte[8192];
        ByteBuffer body = null;
        int contentLength = 0;
        Map<String, String> headers = new HashMap<>();
        StartLine startLine = null;
        Map<String, String> cookies = new HashMap<>();
        int read;

        while ((body == null || body.hasRemaining()) && (read = inputStream.read(buffer)) >= 0) {
            int i = 0;

            if (body == null) {
                for (; i < read; i++) {
                    byte b = buffer[i];

                    if (!currentLine.isEmpty() && currentLine.charAt(currentLine.length() - 1) == '\r' && b == '\n') {
                        if (currentLine.length() == 1) {
                            i++;
                            body = ByteBuffer.allocate(contentLength);
                            break;
                        }

                        String line = currentLine.substring(0, currentLine.length() - 1);
                        currentLine = new StringBuilder();

                        if (startLine == null) {
                            Matcher matcher = START_LINE_PATTERN.matcher(line);
                            if (!matcher.matches()) {
                                throw new IOException("invalid method line");
                            }
                            HttpMethod method = HttpMethod.valueOf(matcher.group(1).toUpperCase(Locale.ROOT));
                            startLine = new StartLine(method, matcher.group(2), matcher.group(3));
                        } else {
                            Matcher matcher = HEADER_PATTERN.matcher(line);
                            if (matcher.matches()) {
                                String key = matcher.group(1).toLowerCase(Locale.ROOT);
                                String value = matcher.group(2);
                                headers.put(key, value);
                                if ("content-length".equals(key)) {
                                    contentLength = Integer.parseInt(value);
                                } else if ("cookie".equals(key)) {
                                    Matcher cmatcher = COOKIE_PATTERN.matcher(value);
                                    while (cmatcher.find()) {
                                        cookies.put(cmatcher.group(1), cmatcher.group(2));
                                    }
                                }
                            } else {
                                throw new IOException("invalid header");
                            }
                        }
                    } else {
                        currentLine.append((char) b);
                    }
                }
            }

            if (body != null) {
                int len = Math.min(body.remaining(), read - i);
                if (len > 0) {
                    body.put(buffer, i, len);
                }
            }
        }

        if (startLine == null || body == null) {
            throw new IOException("incomplete request");
        }

        return new HttpRequest(
                startLine,
                new HttpHeaders(Collections.unmodifiableMap(headers)),
                new HttpCookies(Collections.unmodifiableMap(cookies)),
                body.array());
    }

    public HttpMethod method() {
        return startLine.method;
    }

    public String path() {
        return startLine.path;
    }

    public HttpForm form() {
        return HttpForm.from(body);
    }
}
