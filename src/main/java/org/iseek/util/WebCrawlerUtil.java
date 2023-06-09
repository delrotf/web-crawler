package org.iseek.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.CommandLine;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.iseek.crawler.Crawler;
import org.iseek.crawler.impl.WebCrawler;
import org.iseek.crawler.properties.WebCrawlProperties;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * Utility class for the web crawler
 *
 * @author Tanny del Rosario
 */
@UtilityClass
@Slf4j
public class WebCrawlerUtil {
    public static WebCrawlProperties getDefaultCrawlerProperties() {
        return WebCrawlProperties.builder()
                .maxDepth(getDefaultMaxDepth())
                .maxRetries(getDefaultMaxRetries())
                .initialHostOnly(getDefaultInitialHostOnly())
                .build();
    }
    /**
     * Gets the default max depth from properties file or assigned default value.
     *
     * @return Returns the default max depth.
     */
    public static Integer getDefaultMaxDepth() {
        return PropertiesUtil.getAs(PropertiesUtil.MAX_DEPTH_KEY, WebCrawler.DEFAULT_MAX_DEPTH, Integer.class);
    }

    /**
     * Gets the default max retries from properties file or assigned default value.
     *
     * @return Returns the default max retries.
     */
    public static Integer getDefaultMaxRetries() {
        return PropertiesUtil.getAs(PropertiesUtil.MAX_RETRIES_KEY, WebCrawler.DEFAULT_MAX_RETRIES, Integer.class);
    }

    /**
     * Gets the default initial host only flag from properties file or assigned default value.
     *
     * @return Returns the default initial host only flag.
     */
    public static Boolean getDefaultInitialHostOnly() {
        return PropertiesUtil.getAs(PropertiesUtil.INITIAL_HOST_ONLY_KEY, WebCrawler.DEFAULT_INITIAL_HOST_ONLY, Boolean.class);
    }

    /**
     * Extracts data from the document.
     *
     * @param url The url of the document, to be included as data item.
     * @param document The document being parsed for data extraction.
     * @return Returns an array of string that is the data item.
     */
    public static String[] extractData(String url, Document document) {
        String pageTitle = document.title();

        String metadata = getMetadata(document);

        return new String[]{url, pageTitle, metadata};
    }

    /**
     * Extracts the metadata from the html document.
     *
     * @param document The document to parse.
     * @return Returns a string containing the combined values of metadata.
     */
    public static String getMetadata(Document document) {
        Elements metaTags = document.select("meta");
        StringBuilder metadataBuilder = new StringBuilder();

        for (Element metaTag : metaTags) {
            String name = metaTag.attr("name");
            String content = metaTag.attr("content");
            metadataBuilder.append(name).append(": ").append(content).append(", ");
        }

        return metadataBuilder.toString();
    }

    /**
     *  Creates an httpClient with support for https
     *
     * @return returns a closeable http client
     */
    public static CloseableHttpClient createHttpClient() {
        RequestConfig requestConfig = RequestConfig.custom()
                .setCircularRedirectsAllowed(true)
                .build();

        try {
            SSLContext sslContext = SSLContextBuilder.create().loadTrustMaterial((chain, authType) -> true).build();
            SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);

            return HttpClients.custom()
                    .setDefaultRequestConfig(requestConfig)
                    .setSSLSocketFactory(sslSocketFactory)
                    .build();
        } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
            log.warn("Failed to create and http client. Default http client will be used.", e);
        }

        return HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .build();
    }

    /**
     * Adds a fake user agent to request header to prevent 419 status code.
     *
     * @param httpGet The request object where to add the header.
     */
    public static void addUserAgentHeader(HttpGet httpGet) {
        // fake user agent to prevent 419 status code.
        String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36";
        httpGet.addHeader("User-Agent", userAgent);
   }

    /**
     * Creates a web crawler and starts crawling.
     *
     * @param webCrawlProperties The parameter object.
     * @return Returns the data from crawling.
     */
    public static List<String[]> crawl(WebCrawlProperties webCrawlProperties) {
        Crawler crawler = new WebCrawler(webCrawlProperties);
        return crawler.crawl();
    }

    public static WebCrawlProperties getEffectiveCrawlProperties(WebCrawlProperties defaultWebCrawlProperties, CommandLine commandLine) {
        String crawlUrl = commandLine.getOptionValue(PropertiesUtil.URL_KEY);
        return WebCrawlProperties.builder()
                .url(crawlUrl)
                .maxDepth(CommandLineUtil.getEffectiveValueOf(defaultWebCrawlProperties.getMaxDepth(), commandLine, PropertiesUtil.MAX_DEPTH_KEY, Integer.class))
                .maxRetries(CommandLineUtil.getEffectiveValueOf(defaultWebCrawlProperties.getMaxRetries(), commandLine, PropertiesUtil.MAX_RETRIES_KEY, Integer.class))
                .initialHostOnly(CommandLineUtil.getEffectiveValueOf(defaultWebCrawlProperties.getInitialHostOnly(), commandLine, PropertiesUtil.INITIAL_HOST_ONLY_KEY, Boolean.class))
                .build();
    }
}
