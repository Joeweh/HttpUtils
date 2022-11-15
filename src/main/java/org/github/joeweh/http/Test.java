package org.github.joeweh.http;

public class Test {
    public static void main(String[] args) {
        HttpRequest request = new HttpRequest(HttpMethod.GET, "https://google.com", null);

        HttpResponse response = request.execute();

        System.out.println(response.status());
        System.out.println(response.getCode());
        System.out.println(response.getBody());
    }
}