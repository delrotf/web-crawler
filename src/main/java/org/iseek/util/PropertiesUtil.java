package org.iseek.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.iseek.crawler.impl.WebCrawler;

import java.io.InputStream;
import java.util.Properties;

/**
 * Utility class for the getting properties from the config file
 *
 * @author Tanny del Rosario
 */
@UtilityClass
@Slf4j
public class PropertiesUtil {
    public static final String MAX_DEPTH_KEY = "max.depth";
    public static final String MAX_RETRIES_KEY = "max.retries";
    public static final String INITIAL_HOST_ONLY_KEY = "initial.host.only";
    public static final String OUTPUT_DIRECTORY_KEY = "output.directory";
    public static final String FILENAME_PREFIX_KEY = "filename.prefix";
    public static final String PATH_TO_FILE = "path.to.file";

    public static final String HELP_KEY = "help";
    public static final String URL_KEY = "url";

    private static final Properties properties = loadProperties();

    /**
     * Gets the value of the property.
     *
     * @param propertyName The name of the property.
     * @return Returns the value of the property.
     */
    public static String get(String propertyName) {
        return getAs(propertyName, null);
    }

    /**
     * Gets the value of the property.
     *
     * @param propertyName The name of the property.
     * @param defaultValue The default value when property is not found.
     * @return Returns the value of the property.
     */
    public static String get(String propertyName, String defaultValue) {
        return getAs(propertyName, defaultValue, null);
    }

    /**
     * Gets the value of the property.
     *
     * @param propertyName The name of the property.
     * @param type The type of the returned value.
     * @return Returns the value of the property.
     */
    public static <T> T getAs(String propertyName, Class<T> type) {
        return getAs(propertyName, null, type);
    }

    /**
     * Gets the value of the property.
     *
     * @param propertyName The name of the property.
     * @param defaultValue The default value when property is not found.
     * @param type The type of the returned value.
     * @return Returns the value of the property.
     */
    public static <T> T getAs(String propertyName, T defaultValue, Class<T> type) {
        String value = properties.getProperty(propertyName);
        if (value == null) {
            return defaultValue;
        }

        return convertTypeOf(type, value);
    }

    /**
     * Converts the type of value as the specified type.
     *
     * @param type The type to convert to.
     * @param value The value.
     * @return Returns the converted value.
     * @param <T> The type parameter.
     */
    public static <T> T convertTypeOf(Class<T> type, String value) {
        if (type == null || type == String.class) {
            return (T) value;
        } else if (type == Integer.class) {
            return type.cast(Integer.valueOf(value));
        } else if (type == Boolean.class) {
            return type.cast(Boolean.valueOf(value));
        }

        throw new IllegalArgumentException("Unsupported property type: " + type.getName());
    }

    private static Properties loadProperties() {
        Properties properties = new Properties();

        try (InputStream defaultInputStream = WebCrawler.class.getResourceAsStream("/config.properties")) {
            properties.load(defaultInputStream);
        } catch (Exception e) {
            log.warn("Failed to load config file. Default values will be used.", e);
        }

        return properties;
    }
}
