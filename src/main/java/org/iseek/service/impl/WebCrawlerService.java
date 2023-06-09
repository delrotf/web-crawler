package org.iseek.service.impl;

import lombok.RequiredArgsConstructor;
import org.iseek.crawler.properties.WebCrawlProperties;
import org.iseek.repository.properties.CsvStoreProperties;
import org.iseek.service.CrawlerService;
import org.iseek.util.CsvStoreUtil;
import org.iseek.util.WebCrawlerUtil;

import java.util.List;
@RequiredArgsConstructor
public class WebCrawlerService implements CrawlerService {
    private final WebCrawlProperties effectiveCrawlProperties;
    private final String pathToFile;

    public void crawl() {
        List<String[]> data = WebCrawlerUtil.crawl(effectiveCrawlProperties);
        CsvStoreUtil.storeDataToCSV(CsvStoreProperties.builder()
                .data(data)
                .pathToFile(pathToFile)
                .build());
    }
}
