package org.iseek;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.*;
import org.iseek.crawler.properties.WebCrawlProperties;
import org.iseek.service.CrawlerService;
import org.iseek.service.impl.WebCrawlerService;
import org.iseek.util.*;

/**
 * The Main class
 *
 * @author Tanny del Rosario
 */
@Slf4j
public class Main {
    public static void main(String[] args) {
        Options options = CommandLineUtil.createCommandLineOptions();

        try {
            CommandLineParser parser = new DefaultParser();
            CommandLine commandLine = parser.parse(options, args);

            if (commandLine.hasOption(PropertiesUtil.HELP_KEY)) {
                CommandLineUtil.displayHelp(options);
            }

            WebCrawlProperties effectiveCrawlProperties = WebCrawlerUtil.getEffectiveCrawlProperties(WebCrawlerUtil.getDefaultCrawlerProperties(), commandLine);

            String pathToFile = PathToFileUtil.createPathToFile(commandLine);

            CrawlerService crawlerService = new WebCrawlerService(effectiveCrawlProperties, pathToFile);
            crawlerService.crawl();
        } catch (ParseException e) {
            // logger is not used since this message is meant for the end user.
            System.err.println("Error parsing command-line arguments: " + e.getMessage());
            CommandLineUtil.displayHelp(options);
        }
    }
}
