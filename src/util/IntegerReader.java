package util;

public record IntegerReader() {
    public static int readOrDefault(String string, int defaultValue) {
        if (string != null) {
            try {
                return Integer.parseInt(string);
            } catch (NumberFormatException e) {
                // ignored
            }
        }
        return defaultValue;
    }
}
