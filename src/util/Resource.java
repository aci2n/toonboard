package util;

import http.HttpException;
import http.HttpStatus;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public record Resource() {
    public static byte[] getResource(String file) {
        try (InputStream resource = Resource.class.getResourceAsStream(file)) {
            if (resource == null) {
                throw new HttpException(HttpStatus.NOT_FOUND);
            }
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            resource.transferTo(out);
            return out.toByteArray();
        } catch (IOException e) {
            throw new HttpException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
