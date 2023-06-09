package org.iseek.util;

import lombok.experimental.UtilityClass;
import org.iseek.repository.ListOfStringArrayStore;
import org.iseek.repository.impl.CsvStore;
import org.iseek.repository.properties.CsvStoreProperties;

/**
 * Utility class for the csv store.
 *
 * @author Tanny del Rosario
 */
@UtilityClass
public class CsvStoreUtil {
    /**
     * Stores the data to a csv file.
     *
     * @param csvStoreProperties The parameter object.
     */
    public static void storeDataToCSV(CsvStoreProperties csvStoreProperties) {
        ListOfStringArrayStore store = new CsvStore(csvStoreProperties);
        store.store();
    }
}
