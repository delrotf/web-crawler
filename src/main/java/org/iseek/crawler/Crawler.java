package org.iseek.crawler;

import java.util.List;

/**
 * A Crawler
 *
 * @author Tanny del Rosario
 */
public interface Crawler {
    /**
     * Crawls a html document from the given url.
     *
     * @return Returns a list of arrays of strings as the data.
     */
    List<String[]> crawl();
}
