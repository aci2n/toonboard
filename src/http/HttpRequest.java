package http;

import util.IntegerReader;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public record HttpRequest(String method, String path, String version, Map<String, String> headers, ByteBuffer body) {
    private final static Pattern METHOD_PATTERN = Pattern.compile("(.*) (.*) (.*)");
    private final static Pattern HEADER_PATTERN = Pattern.compile("(.*): (.*)");

    private record Reader(List<String> lines, byte[] body) {
        private static Reader from(InputStream inputStream) throws IOException {
            StringBuilder line = new StringBuilder();
            byte[] buffer = new byte[4096];
            ByteArrayOutputStream body = new ByteArrayOutputStream();
            List<String> lines = new ArrayList<>();
            boolean reachedBody = false;

            while (inputStream.available() > 0) {
                int read = inputStream.read(buffer);
                int i = 0;

                if (!reachedBody) {
                    for (; i < read; i++) {
                        byte b = buffer[i];

                        if (!line.isEmpty() && line.charAt(line.length() - 1) == '\r' && b == '\n') {
                            if (line.length() == 1) {
                                i++;
                                reachedBody = true;
                                break;
                            }

                            lines.add(line.substring(0, line.length() - 1));
                            line = new StringBuilder();
                        } else {
                            line.append((char) b);
                        }
                    }
                }

                if (reachedBody) {
                    body.write(buffer, i, read - i);
                }
            }

            return new Reader(lines, body.toByteArray());
        }
    }

    static HttpRequest readRequest(InputStream inputStream) throws IOException {
        Reader reader = Reader.from(new BufferedInputStream(inputStream));

        Matcher methodMatcher = METHOD_PATTERN.matcher(reader.lines().get(0));
        if (!methodMatcher.matches()) {
            throw new IOException("invalid method line");
        }
        String method = methodMatcher.group(1);
        String path = methodMatcher.group(2);
        String version = methodMatcher.group(3);

        Map<String, String> headers = new HashMap<>(10);
        for (int i = 1; i < reader.lines().size(); i++) {
            String header = reader.lines.get(i);
            Matcher headerMatcher = HEADER_PATTERN.matcher(header);
            if (headerMatcher.matches()) {
                headers.put(headerMatcher.group(1).toLowerCase(Locale.ROOT), headerMatcher.group(2));
            }
        }

        return new HttpRequest(method, path, version, headers, ByteBuffer.wrap(reader.body));
    }
}
