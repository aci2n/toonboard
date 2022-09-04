package util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public record Resource() {
    public static byte[] getResource(String file) throws IOException {
        try (InputStream resource = Resource.class.getResourceAsStream(file)) {
            if (resource == null) {
                throw new IOException("not found");
            }
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            resource.transferTo(out);
            return out.toByteArray();
        }
    }
}
