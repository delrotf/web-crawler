package org.iseek.crawler;

import org.iseek.crawler.impl.WebCrawler;
import org.iseek.crawler.properties.WebCrawlProperties;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CrawlerTest {

    @Test
    void crawl() {
        String url = "https://www.cochranelibrary.com/cdsr/reviews/topics";
        Crawler webCrawler = new WebCrawler(WebCrawlProperties.builder()
                .url(url)
                .maxDepth(1)
                .build());
        assertFalse(webCrawler.crawl().isEmpty(), "data should not be empty");}
}