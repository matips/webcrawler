package com.skipjaq.webcrawler;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

@Component
public class Webcrawler {
    private final MessageDigest md5;

    public Webcrawler() throws NoSuchAlgorithmException {
        md5 = MessageDigest.getInstance("MD5");
    }

    private byte[] generateHash(String text) {
        return md5.digest(text.getBytes());
    }

    WebPageRecord parse(String url) throws IOException {
        Connection connect = Jsoup.connect(url);
        Document doc = connect.get();
        Elements newsHeadlines = doc.getElementsByTag("a");
        List<String> reachableLinks = newsHeadlines.stream()
                .map(link -> link.attr("abs:href"))
                .filter(link -> link.startsWith("http://"))
                .distinct()
                .collect(Collectors.toList());
        String text = connect.response().body();
        return new WebPageRecord(url, text.length(), generateHash(text), reachableLinks);
    }

    private Stream<WebPageRecord> parseRecursively(String url, int deep, ConcurrentHashMap<String, Object> handledPages) {

        if (deep < 0 || checkAndSetAsHandled(handledPages, url))
            return Stream.empty();

        WebPageRecord result;
        try {
            result = parse(url);
        } catch (IOException e) {
            System.err.println(e.toString());
            return Stream.empty();
        }
        Stream<WebPageRecord> children =
                result.getReachablePages()
                        .stream()
                        .flatMap(
                                page -> parseRecursively(page, deep - 1, handledPages)
                        )
                        .parallel();
        return Stream.concat(Stream.of(result), children);

    }

    private boolean checkAndSetAsHandled(ConcurrentHashMap<String, Object> handledPages, String url) {
        return handledPages.putIfAbsent(url, true) != null;
    }

    public Collection<WebPageRecord> parseRecursively(String url, int deep) throws IOException {
        return parseRecursively(url, deep, new ConcurrentHashMap<>())
                .collect(Collectors.toList());
    }


}
