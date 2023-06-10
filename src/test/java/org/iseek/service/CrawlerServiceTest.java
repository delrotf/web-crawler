package org.iseek.service;

import org.iseek.crawler.properties.WebCrawlProperties;
import org.iseek.repository.properties.PathToFileProperties;
import org.iseek.service.impl.WebCrawlerService;
import org.iseek.util.PathToFileUtil;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CrawlerServiceTest {

    @Test
    void crawl() {
        String url = "https://www.cochranelibrary.com/cdsr/reviews/topics";
        String outputDirectory = PathToFileUtil.getDefaultOutputDirectory();
        String fileNamePrefix = PathToFileUtil.getDefaultFilenamePrefix();

        String pathToFile = PathToFileUtil.assemblePathToFile(PathToFileProperties.builder()
                        .outputDirectory(outputDirectory)
                        .filenamePrefix(fileNamePrefix)
                .build());

        CrawlerService webCrawler = new WebCrawlerService(WebCrawlProperties.builder()
                .url(url)
                .maxDepth(1)
                .build(), pathToFile);
        assertDoesNotThrow(webCrawler::crawl);
        assertTrue(new File(pathToFile).exists(), "Output file should exist");
    }
}