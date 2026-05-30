package edu.unac.external;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class OpenLibraryClient {

    private final RestTemplate restTemplate = new RestTemplate();

    public String searchBooks(String title, String author) {

        StringBuilder url = new StringBuilder("https://openlibrary.org/search.json?");

        boolean hasParams = false;

        if (title != null && !title.isBlank()) {

            url.append("title=")
                    .append(URLEncoder.encode(title, StandardCharsets.UTF_8));

            hasParams = true;
        }

        if (author != null && !author.isBlank()) {

            if (hasParams) {
                url.append("&");
            }

            url.append("author=")
                    .append(URLEncoder.encode(author, StandardCharsets.UTF_8));
        }

        System.out.println("OPENLIBRARY URL: " + url);

        return restTemplate.getForObject(url.toString(), String.class);
    }

    public String getBookByKey(String key) {

        String url = "https://openlibrary.org" + key + ".json";

        return restTemplate.getForObject(url, String.class);
    }
}