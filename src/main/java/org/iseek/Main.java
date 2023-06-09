package org.iseek;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.util.ContextInitializer;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.*;
import org.iseek.crawler.properties.WebCrawlProperties;
import org.iseek.service.CrawlerService;
import org.iseek.service.impl.WebCrawlerService;
import org.iseek.util.CommandLineUtil;
import org.iseek.util.PathToFileUtil;
import org.iseek.util.PropertiesUtil;
import org.iseek.util.WebCrawlerUtil;
import org.slf4j.LoggerFactory;

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

            configureLoggerVerbosity(commandLine);

            if (commandLine.hasOption(PropertiesUtil.HELP_KEY)) {
                CommandLineUtil.displayHelp(options);
            }
            // will show even when logging is disabled.
            System.out.println("Processing...");

            WebCrawlProperties effectiveCrawlProperties = WebCrawlerUtil.getEffectiveCrawlProperties(WebCrawlerUtil.getDefaultCrawlerProperties(), commandLine);

            String pathToFile = PathToFileUtil.createPathToFile(commandLine);

            CrawlerService crawlerService = new WebCrawlerService(effectiveCrawlProperties, pathToFile);
            crawlerService.crawl();

            // will show even when logging is disabled.
            System.out.println("Done.");
        } catch (ParseException e) {
            // logger is not used since this message is meant for the end user.
            System.err.println("Error parsing command-line arguments: " + e.getMessage());
            CommandLineUtil.displayHelp(options);
        }
    }

    private static void configureLoggerVerbosity(CommandLine commandLine) {
        if (commandLine.hasOption(PropertiesUtil.VERBOSE_KEY)) {
            System.setProperty("verbose", "true");
        }

        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();

        ContextInitializer initializer = new ContextInitializer(loggerContext);
        try {
            initializer.autoConfig();
        } catch (JoranException e) {
            log.warn("Failed to initialize the logger used for logging.", e);
        }

        if (commandLine.hasOption(PropertiesUtil.LOGGER_KEY)) {
            // Print the internal status of Logback
            StatusPrinter.printInCaseOfErrorsOrWarnings(loggerContext);
        }
    }
}
