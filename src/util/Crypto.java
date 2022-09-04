package util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public record Crypto() {
    public static byte[] sha1(String value) {
        final MessageDigest crypt;
        try {
            crypt = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        crypt.reset();
        crypt.update(value.getBytes(StandardCharsets.UTF_8));
        return crypt.digest();
    }
}
