package org.github.joeweh.http;

import java.util.HashMap;

public class HttpHeaderMap {
    private final HashMap<String, String> headers;

    public HttpHeaderMap() {
        this.headers = new HashMap<>();
    }

    public void add(String key, String value) {
        this.headers.put(key, value);
    }

    public HashMap<String, String> getKeyValuePairs() {
        return this.headers;
    }
}