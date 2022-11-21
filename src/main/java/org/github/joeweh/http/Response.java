package org.github.joeweh.http;

import java.util.List;
import java.util.Map;

public class Response {
    private final boolean success;
    private final int responseCode;
    private final String responseBody;
    private final Map<String, List<String>> headers;

    public Response(boolean success, int responseCode, Map<String, List<String>> headers, String responseBody) {
        this.success = success;
        this.responseCode = responseCode;
        this.headers = headers;
        this.responseBody = responseBody;
    }

    public boolean isSuccessful()
    {
        return this.success;
    }

    public int getCode()
    {
        return this.responseCode;
    }

    public Map<String, List<String>> getHeaders() { return this.headers; }

    public String getBody()
    {
        return this.responseBody;
    }
}