package org.github.joeweh.http;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

import java.net.URL;
import java.util.Map;
import java.util.concurrent.Callable;

public class HttpRequest implements Callable<HttpResponse> {
    private final String urlString;
    private final HttpMethod method;
    private final String requestBody;
    private final HttpHeaderMap headers;

    private HttpsURLConnection con;

    public HttpRequest(HttpMethod method, String url, HttpHeaderMap properties)
    {
        this.urlString = url;
        this.method = method;
        this.headers = properties;
        this.requestBody = null;
    }

    public HttpRequest(HttpMethod method, String url, HttpHeaderMap properties, String requestBody)
    {
        this.urlString = url;
        this.method = method;
        this.headers = properties;
        this.requestBody = requestBody;
    }

    public HttpResponse execute() {
        try {
            URL url = new URL(this.urlString);

            this.con = (HttpsURLConnection) url.openConnection();

            this.con.setRequestMethod(this.method.toString());

            if (this.headers != null) {
                for (Map.Entry<String, String> entry : this.headers.getKeyValuePairs().entrySet())
                {
                    this.con.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }

            if (this.requestBody != null)
            {
                try
                {
                    this.con.setDoOutput(true);

                    OutputStream stream = this.con.getOutputStream();
                    stream.write(this.requestBody.getBytes());
                    stream.flush();
                    stream.close();
                }

                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }

            return processResponse();
        }

        catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private HttpResponse processResponse() {
        try {
            int responseCode = this.con.getResponseCode();

            if (this.isSuccessful(responseCode)) {
                // Read & Return Success Response
                BufferedReader in = new BufferedReader(new InputStreamReader(this.con.getInputStream()));

                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

                in.close();

                return new HttpResponse(true, responseCode, response.toString());
            }

            // Return Error Response
            return new HttpResponse(false, responseCode, null);
        }

        catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private boolean isSuccessful(int responseCode)
    {
        boolean hasSucceeded = false;

        switch (this.method)
        {
            case GET:
                if (HttpsURLConnection.HTTP_OK == responseCode)
                {
                    hasSucceeded = true;
                }
                break;

            case POST:
                if (HttpsURLConnection.HTTP_OK == responseCode || HttpsURLConnection.HTTP_CREATED == responseCode)
                {
                    hasSucceeded = true;
                }

                break;

            case PUT:
                if (HttpsURLConnection.HTTP_OK == responseCode)
                {
                    hasSucceeded = true;
                }

                break;

            case DELETE:
                if (HttpsURLConnection.HTTP_OK == responseCode)
                {
                    hasSucceeded = true;
                }

                break;
        }

        return hasSucceeded;
    }

    @Override
    public HttpResponse call() {
        return this.execute();
    }
}