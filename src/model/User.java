package model;

import util.Crypto;

public record User(String name, byte[] password) {
    public User(String name) {
        this(name, new byte[0]);
    }

    public User(String name, String password) {
        this(name, Crypto.sha1(password));
    }
}
