package http;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public record HttpForm(Map<String, String> entries) {
    private static final Pattern ENTRY_PATTERN = Pattern.compile(("([^=&]+)(?:=([^&]*))?"));

    public static HttpForm from(byte[] body) {
        String str = new String(body, StandardCharsets.UTF_8);
        Matcher matcher = ENTRY_PATTERN.matcher(str);
        Map<String, String> entries = new HashMap<>();

        while (matcher.find()) {
            String key = matcher.group(1);
            String value = matcher.group(2);
            entries.put(key, value);
        }

        return new HttpForm(entries);
    }

    public boolean has(String key) {
        return entries.containsKey(key);
    }

    public String get(String key) {
        return entries.get(key);
    }
}
