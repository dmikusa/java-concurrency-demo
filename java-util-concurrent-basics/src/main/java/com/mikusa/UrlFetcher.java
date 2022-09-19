package com.mikusa;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;

public class UrlFetcher {
    private final HttpClient client;

    public UrlFetcher() {
        client = HttpClient.newBuilder()
                .followRedirects(Redirect.NORMAL)
                .connectTimeout(Duration.ofSeconds(5))
                .build();
    }

    public String fetch(String url) {
        HttpResponse<String> resp;
        try {
            resp = client.send(
                    HttpRequest.newBuilder()
                            .uri(URI.create(url))
                            .GET()
                            .build(),
                    BodyHandlers.ofString());
            return resp.body();
        } catch (IOException e) {
            System.err.println("IOException fetching: " + url);
            // e.printStackTrace();
        } catch (InterruptedException e) {
            System.err.println("InterruptedException fetching: " + url);
            // e.printStackTrace();
        }
        return "";
    }
}
