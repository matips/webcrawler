package com.skipjaq.webcrawler;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.github.tomakehurst.wiremock.junit.WireMockRule;

public class WebcrawlerTest {
    private static final int PORT = 8087;
    private static final String LOCALHOST = "http://localhost:" + PORT;

    private Webcrawler webcrawler;
    @Rule
    public WireMockRule wireMockRule = new WireMockRule(PORT);
    private String wikipediaContent;
    private String simpleContent;
    private String simpleContent2;
    private String simpleContent3;

    @Before
    public void setUp() throws Exception {
        webcrawler = new Webcrawler();
        wikipediaContent = getFileContent("wikipedia.html");
        simpleContent = getFileContent("simple.html");
        simpleContent2 = getFileContent("simple2.html");
        simpleContent3 = getFileContent("simple3.html");

        stubFor(get(urlEqualTo("/wiki"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "text/plain")
                        .withBody(wikipediaContent)));
        stubFor(get(urlEqualTo("/simple.html"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "text/plain")
                        .withBody(simpleContent)));
        stubFor(get(urlEqualTo("/simple2.html"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "text/plain")
                        .withBody(simpleContent2)));
        stubFor(get(urlEqualTo("/simple3.html"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "text/plain")
                        .withBody(simpleContent3)));

    }

    private String getFileContent(String name) throws IOException {
        return IOUtils.toString(
                Thread.currentThread().getContextClassLoader().getResourceAsStream(name));
    }

    @org.junit.Test
    public void parseSimplePage() throws Exception {
        WebPageRecord result = webcrawler.parse(LOCALHOST + "/simple.html");
        assertEquals(simpleContent.length(), result.contentSize);
        assertArrayEquals(
                new byte[] { (byte) 0xDD, (byte) 0xED, (byte) 0xEC, (byte) 0xE2, (byte) 0x8D, (byte) 0x38, (byte) 0xDA, (byte) 0x32, (byte) 0xC1, (byte) 0xE3,
                        (byte) 0x00, (byte) 0xA7, (byte) 0x72, (byte) 0x9A, (byte) 0x12, (byte) 0x9D }
                , result.md5);
        assertEquals(3, result.reachablePages.size());
        assertTrue(result.reachablePages.contains(LOCALHOST + "/simple.html"));
        assertTrue(result.reachablePages.contains(LOCALHOST + "/simple2.html"));
        assertTrue(result.reachablePages.contains(LOCALHOST + "/simple3.html"));
    }

    @Test
    public void parseRecursively() throws IOException {
        Collection<WebPageRecord> results = webcrawler.parseRecursively(LOCALHOST + "/simple.html", 10);
        assertEquals(4, results.size());
        List<Integer> sizes = results.stream()
                .map(WebPageRecord::getContentSize)
                .map(Long::intValue)
                .collect(Collectors.toList());
        assertTrue(sizes.contains(wikipediaContent.length()));
        assertTrue(sizes.contains(simpleContent.length()));
        assertTrue(sizes.contains(simpleContent2.length()));
        assertTrue(sizes.contains(simpleContent3.length()));
    }

    @Test
    public void parseRecursivelyNotDeep() throws IOException {
        Collection<WebPageRecord> results = webcrawler.parseRecursively(LOCALHOST + "/simple.html", 1);
        assertEquals(3, results.size());
        List<Integer> sizes = results.stream()
                .map(WebPageRecord::getContentSize)
                .map(Long::intValue)
                .collect(Collectors.toList());
        assertFalse(sizes.contains(wikipediaContent.length()));
        assertTrue(sizes.contains(simpleContent.length()));
        assertTrue(sizes.contains(simpleContent2.length()));
        assertTrue(sizes.contains(simpleContent3.length()));
    }

    @Test
    public void parseRecursively0Deep() throws IOException {
        Collection<WebPageRecord> results = webcrawler.parseRecursively(LOCALHOST + "/simple.html", 0);
        assertEquals(1, results.size());
    }

    @org.junit.Test
    public void parseWikipedia() throws Exception {
        WebPageRecord result = webcrawler.parse(LOCALHOST + "/wiki");
        assertEquals(wikipediaContent.length(), result.contentSize);
    }

}