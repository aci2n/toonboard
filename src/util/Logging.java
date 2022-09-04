package util;

import java.util.logging.Level;
import java.util.logging.Logger;

public record Logging() {
    public static Logger get(Class<?> klass) {
        Logger logger = Logger.getLogger(klass.getName());
        logger.setLevel(Level.ALL);
        return logger;
    }
}
