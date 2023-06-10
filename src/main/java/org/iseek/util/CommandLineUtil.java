package org.iseek.util;

import lombok.experimental.UtilityClass;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.iseek.crawler.properties.WebCrawlProperties;
import org.iseek.repository.properties.PathToFileProperties;

/**
 * Utility class for the command line
 *
 * @author Tanny del Rosario
 */
@UtilityClass
public class CommandLineUtil {
    /**
     * Creates command line options used for crawling an url.
     *
     * @param options The options for the command line.
     * @param webCrawlProperties The properties for the web crawler.
     */
    public static void createCrawlerOptions(Options options, WebCrawlProperties webCrawlProperties) {
        options.addOption("h", PropertiesUtil.HELP_KEY, false, "Display help information.");
        options.addOption("v", PropertiesUtil.VERBOSE_KEY, false, "Shows logs to the console.");
        options.addOption("l", PropertiesUtil.LOGGER_KEY, false, "Show logs from logger.");

        options.addRequiredOption("u", PropertiesUtil.URL_KEY, true, "The url to initiate crawling with. Required.");

        String maxDepthDesc = String.format("Crawling depth limit. 1 means only the supplied url will be crawled, not the embedded urls. Default is %d, with no limit.", webCrawlProperties.getMaxDepth());
        options.addOption("d", PropertiesUtil.MAX_DEPTH_KEY, true, maxDepthDesc);

        String maxRetriesDesc = String.format("Number of retries when crawling a url failed. Default is %d.", webCrawlProperties.getMaxRetries());
        options.addOption("r", PropertiesUtil.MAX_RETRIES_KEY, true, maxRetriesDesc);

        String initialHostOnlyDesc = String.format("Crawl on the supplied host only. Default is %s.", webCrawlProperties.getInitialHostOnly());
        options.addOption("ho", PropertiesUtil.INITIAL_HOST_ONLY_KEY, true, initialHostOnlyDesc);
    }

    /**
     * Creates command line options used for creating file path.
     *
     * @param pathToFileProperties The properties for file path.
     * @param options The options for the command line.
     */
    public static void createPathToFileOptions(Options options, PathToFileProperties pathToFileProperties) {
        String outputDirectoryDesc = String.format("Output directory for the output csv file. Default is %s.", pathToFileProperties.getOutputDirectory());
        options.addOption("o", PropertiesUtil.OUTPUT_DIRECTORY_KEY, true, outputDirectoryDesc);

        String filenamePrefixDesc = String.format("The prefix for output filename. Default is %s.", pathToFileProperties.getFilenamePrefix());
        options.addOption("p", PropertiesUtil.FILENAME_PREFIX_KEY, true, filenamePrefixDesc);
    }

    /**
     * Creates command line options used for the writer.
     *
     * @param options The options for the command line.
     */
    public static void createWriterOptions(Options options) {
        options.addOption("f", PropertiesUtil.PATH_TO_FILE, true, "This overrides the default output path");
    }

    /**
     * Displays help in command line
     *
     * @param options The options for the command line.
     */
    public static void displayHelp(Options options) {
        String header = "This crawls any given url and then saves the data to a csv file in output directory. Logs can be found under logs directory.";
        String footer = "e.g. java -jar web-crawler.jar -u http://google.com -d 1";

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("java -jar web-crawler.jar", header, options, footer, true);
    }

    /**
     * Gets the effective value of a property. The value can either be the default or command line.
     *
     * @param defaultValue The default value.
     * @param commandLine The command line where the value is checked.
     * @param propertyKey The name of the property to get.
     * @return Returns the effective value, either the default or the one from command line.
     * @param <T> The return type.
     */
    public static <T> T getEffectiveValueOf(T defaultValue, CommandLine commandLine, String propertyKey) {
        return getEffectiveValueOf(defaultValue, commandLine, propertyKey, null);
    }

    /**
     * Gets the effective value of a property. The value can either be the default or command line.
     *
     * @param defaultValue The default value.
     * @param commandLine The command line where the value is checked.
     * @param propertyKey The name of the property to get.
     * @param type The type of the value.
     * @return Returns the effective value, either the default or the one from command line.
     * @param <T> The return type.
     */
    public static <T> T getEffectiveValueOf(T defaultValue, CommandLine commandLine, String propertyKey, Class<T> type) {
        if (!commandLine.hasOption(propertyKey)) {
            return defaultValue;
        } else {
            String newValue = commandLine.getOptionValue(propertyKey);
            return PropertiesUtil.convertTypeOf(type, newValue);
        }
    }

    /**
     * Creates commandline options.
     *
     * @return Returns the created options.
     */
    public static Options createCommandLineOptions() {
        Options options = new Options();
        WebCrawlProperties defaultCrawlerProperties = WebCrawlerUtil.getDefaultCrawlerProperties();
        PathToFileProperties defaultPathToFileProperties = PathToFileUtil.getDefaultPathToFileProperties();

        createCrawlerOptions(options, defaultCrawlerProperties);
        createPathToFileOptions(options, defaultPathToFileProperties);
        createWriterOptions(options);
        return options;
    }
}
