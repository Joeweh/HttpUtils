package org.github.joeweh.http;

public class HttpResponse {
    private final boolean status;
    private final int responseCode;
    private final String responseBody;

    public HttpResponse(boolean success, int responseCode, String responseBody)
    {
        this.status = success;
        this.responseCode = responseCode;
        this.responseBody = responseBody;
    }

    public boolean status()
    {
        return this.status;
    }

    public int getCode()
    {
        return this.responseCode;
    }

    public String getBody()
    {
        return this.responseBody;
    }
}