package org.iseek.crawler.properties;

import lombok.Builder;
import lombok.Value;

/**
 * The properties for WebCrawler.
 *
 * @author Tanny del Rosario
 */
@Value
@Builder
public class WebCrawlProperties {
    String url;
    Integer maxDepth;
    Integer maxRetries;
    Boolean initialHostOnly;
}
