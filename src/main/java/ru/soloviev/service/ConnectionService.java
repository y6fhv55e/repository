package ru.soloviev.service;

import org.jsoup.Jsoup;

import java.io.IOException;

public class ConnectionService {

    public String getJsonFromUrl(String url) throws IOException {
        return Jsoup.connect(url)
                .ignoreContentType(true)
                .execute().body();
    }
}