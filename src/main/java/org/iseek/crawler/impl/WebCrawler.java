package org.iseek.crawler.impl;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.iseek.crawler.Crawler;
import org.iseek.crawler.properties.WebCrawlProperties;
import org.iseek.util.WebCrawlerUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

/**
 * A web crawler
 *
 * @author Tanny del Rosario
 */
@Data
@Slf4j
public class WebCrawler implements Crawler {
    private final Set<String> collectedUrls = new HashSet<>(); // used to prevent processing same url multiple times.
    private final List<String[]> data = new ArrayList<>(); // crawled data.
    private String initialProtocol; // used when the embedded url has missing protocol.
    private String initialHost; // used when the embedded url has missing protocol.
    String urlString;
    private Integer maxDepth;
    private Integer maxRetries;
    private Boolean initialHostOnly;

    private final WebCrawlProperties webCrawlProperties;

    public static final int DEFAULT_MAX_DEPTH = 0;
    public static final int DEFAULT_MAX_RETRIES = 1;
    public static final boolean DEFAULT_INITIAL_HOST_ONLY = true;
    @Override
    public List<String[]> crawl() {
        log.info("Crawling with params: {}", webCrawlProperties);

        urlString = webCrawlProperties.getUrl();

        maxDepth = webCrawlProperties.getMaxDepth();
        maxDepth = maxDepth != null ? maxDepth : DEFAULT_MAX_DEPTH;

        maxRetries = webCrawlProperties.getMaxRetries();
        maxRetries = maxRetries != null ? maxRetries : DEFAULT_MAX_RETRIES;

        initialHostOnly = webCrawlProperties.getInitialHostOnly();
        initialHostOnly = initialHostOnly != null ? initialHostOnly : DEFAULT_INITIAL_HOST_ONLY;

        if (urlString == null) {
            throw new IllegalArgumentException("Missing url argument.");
        } else {

            try {
                URL url = new URL(urlString);

                initialProtocol = url.getProtocol();
                initialHost = url.getHost();
                log.debug("initial protocol: {}; host: {}", initialProtocol, initialHost);
            } catch (MalformedURLException e) {
                throw new IllegalArgumentException("Malformed URL", e);
            }

            collectedUrls.add(urlString);
        }

        return crawl(urlString, 0, 0);
    }

    private List<String[]> crawl(String urlString, int depthCount, int retryCount) {
        if (maxDepth > 0 && depthCount >= maxDepth) {
            log.info("Crawl depth limit of {} has been reached. Skipping {}", maxDepth, urlString);
            return data;
        }

        log.info("Crawling at depth {}...; Url: {}", depthCount, urlString);

        HttpGet httpGet = new HttpGet(urlString);

        WebCrawlerUtil.addUserAgentHeader(httpGet);

        //Jsoup can also connect to the server, but it is required to use HTTPClient, so I'm using it here.
        try (CloseableHttpClient client = WebCrawlerUtil.createHttpClient();
             CloseableHttpResponse response = client.execute(httpGet)) {

            int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode == HttpStatus.SC_OK) {
                processResponse(urlString, depthCount, retryCount, response);
            } else {
                log.info("Skipping response with status of {}", statusCode);
            }

            log.info("Done crawling {}", urlString);
        } catch (IOException | RuntimeException e) {
            if (maxRetries > 0 && retryCount < maxRetries) {
                log.warn("Failed to crawl but will try again. Url: {}", urlString, e);
                log.info("Retrying the failed crawl. retries: {}; retryCount: {}; Url: {}", maxRetries, retryCount, urlString);
                crawl(urlString, depthCount, retryCount + 1);
            } else {
                log.error("Failed to crawl {}", urlString, e);
            }
        }

        return data;
    }

    private void processResponse(String urlString, int depthCount, int retryCount, CloseableHttpResponse response) throws IOException {
        HttpEntity entity = response.getEntity();
        String htmlContent = EntityUtils.toString(entity);

        Document document = Jsoup.parse(htmlContent);

        String[] item = WebCrawlerUtil.extractData(urlString, document);

        if (log.isDebugEnabled()) {
            log.debug("item: {}", Arrays.toString(item));
        }

        data.add(item);

        crawlEmbeddedLinks(document, depthCount, retryCount);
    }

    private void crawlEmbeddedLinks(Document document, int depthCount, int retryCount) {
        Elements links = getLinks(document);

        for (Element link : links) {
            String href = link.attr("href");

            if (href.startsWith("/")) {
                href = initialProtocol + "://" + initialHost + href;
            }

            try {
                new URL(href); // just to check if url is malformed or not.

                if (collectedUrls.contains(href) || (initialHostOnly && !href.contains(initialHost))) {
                    if (Boolean.TRUE.equals(initialHostOnly) && !href.contains(initialHost)) {
                        log.info("Url does not contain the initial host. host: {}. Skipping {}", initialHost, href);
                    } else {
                        log.info("Url has already been processed. Skipping {}", href);
                    }
                } else {
                    collectedUrls.add(href);
                    crawl(href, depthCount + 1, retryCount);
                }
            } catch (MalformedURLException e) {
                log.info("Skipping a malformed url. Url: {}", href);
            }
        }
    }

    private Elements getLinks(Document document) {
        Elements links;
        if (Boolean.TRUE.equals(initialHostOnly)) {
            links = document.select("a[href*=\"" + initialHost + "\"]");
        } else {
            links = document.select("a[href]");
        }
        return links;
    }
}
