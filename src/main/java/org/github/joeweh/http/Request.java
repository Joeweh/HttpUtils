package org.github.joeweh.http;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

import java.net.URL;

import java.util.Map;
import java.util.concurrent.Callable;

public class Request implements Callable<Response> {
    private final String urlString;
    private final Method method;
    private final String requestBody;
    private final Map<String, String> headers;

    private HttpsURLConnection con;

    public Request(Method method, String url, Map<String, String> headers) {
        this.urlString = url;
        this.method = method;
        this.headers = headers;
        this.requestBody = null;
    }

    public Request(Method method, String url, Map<String, String> headers, String requestBody) {
        this.urlString = url;
        this.method = method;
        this.headers = headers;
        this.requestBody = requestBody;
    }

    public Response execute() {
        try {
            URL url = new URL(this.urlString);

            this.con = (HttpsURLConnection) url.openConnection();

            this.con.setRequestMethod(this.method.toString());

            if (this.headers != null) {
                for (Map.Entry<String, String> entry : this.headers.entrySet())
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

    private Response processResponse() {
        try {
            int responseCode = this.con.getResponseCode();

            if (this.isSuccessful(responseCode)) {
                BufferedReader in = new BufferedReader(new InputStreamReader(this.con.getInputStream()));

                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

                in.close();

                return new Response(true, responseCode, this.con.getHeaderFields(), response.toString());
            }

            return new Response(false, responseCode, this.con.getHeaderFields(), null);
        }

        catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private boolean isSuccessful(int responseCode) {
        return responseCode > 199 && responseCode < 300;
    }

    @Override
    public Response call() {
        return this.execute();
    }

    public enum Method { GET, POST, PUT, DELETE }
}