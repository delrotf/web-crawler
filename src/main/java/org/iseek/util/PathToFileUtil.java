package org.iseek.util;

import lombok.experimental.UtilityClass;
import org.apache.commons.cli.CommandLine;
import org.iseek.repository.properties.PathToFileProperties;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utility class for the file path.
 *
 * @author Tanny del Rosario
 */
@UtilityClass
public class PathToFileUtil {
    private static final String PATH_SEPARATOR = "/";
    public static final String DEFAULT_OUTPUT_DIRECTORY = "out";
    public static final String DEFAULT_FILENAME_PREFIX = "output";

    /**
     * Gets the default file path properties.
     *
     * @return Returns the default file path properties.
     */
    public static PathToFileProperties getDefaultPathToFileProperties() {
        return PathToFileProperties.builder()
                .outputDirectory(getDefaultOutputDirectory())
                .filenamePrefix(getDefaultFilenamePrefix())
                .build();
    }

    /**
     * Gets the default output directory
     *
     * @return Returns the default output directory.
     */
    public static String getDefaultOutputDirectory() {
        return PropertiesUtil.get(PropertiesUtil.OUTPUT_DIRECTORY_KEY, DEFAULT_OUTPUT_DIRECTORY);
    }

    /**
     * Gets the default filename prefix.
     *
     * @return Returns the default filename prefix.
     */
    public static String getDefaultFilenamePrefix() {
        return PropertiesUtil.get(PropertiesUtil.FILENAME_PREFIX_KEY, DEFAULT_FILENAME_PREFIX);
    }

    /**
     * Creates the path to the csv file.
     *
     * @param pathToFileProperties The path/to/file properties object
     * @return Returns the created path to csv file.
     */
    public static String getEffectivePathToFile(String pathToFile, PathToFileProperties pathToFileProperties) {
        if (pathToFile == null) {
            pathToFile = assemblePathToFile(pathToFileProperties);
        }
        return pathToFile;
    }

    public static String assemblePathToFile(PathToFileProperties pathToFileProperties) {
        String dateFormat = "yyyyMMdd_HHmmss";
        String timestamp = new SimpleDateFormat(dateFormat).format(new Date());
        return String.format("%s%s%s_%s.csv",
                pathToFileProperties.getOutputDirectory(),
                PATH_SEPARATOR,
                pathToFileProperties.getFilenamePrefix(),
                timestamp);
    }

    /**
     * Gets the effective properties of file path.
     *
     * @param defaultPathToFileProperties The default path/to/file properties.
     * @param commandLine The command line where to get the values from.
     * @return Returns the properties of file path.
     */
    public static PathToFileProperties getEffectivePathToFileProperties(PathToFileProperties defaultPathToFileProperties, CommandLine commandLine) {
        return PathToFileProperties.builder()
                .outputDirectory(CommandLineUtil.getEffectiveValueOf(defaultPathToFileProperties.getOutputDirectory(), commandLine, PropertiesUtil.OUTPUT_DIRECTORY_KEY))
                .filenamePrefix(CommandLineUtil.getEffectiveValueOf(defaultPathToFileProperties.getFilenamePrefix(), commandLine, PropertiesUtil.FILENAME_PREFIX_KEY))
                .build();
    }

    /**
     * Creates the path to file.
     *
     * @param commandLine The command line where to get the values from.
     * @return Returns the path to file.
     */
    public static String createPathToFile(CommandLine commandLine) {
        String pathToFile = commandLine.getOptionValue(PropertiesUtil.PATH_TO_FILE);
        pathToFile = getEffectivePathToFile(pathToFile,
                getEffectivePathToFileProperties(getDefaultPathToFileProperties(), commandLine));
        return pathToFile;
    }
}
