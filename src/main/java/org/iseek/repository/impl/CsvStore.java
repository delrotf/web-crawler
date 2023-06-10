package org.iseek.repository.impl;

import com.opencsv.CSVWriter;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.iseek.repository.ListOfStringArrayStore;
import org.iseek.repository.properties.CsvStoreProperties;

import java.io.FileWriter;
import java.io.IOException;

/**
 * A store that uses csv.
 *
 * @author Tanny del Rosario
 */
@Data
@Slf4j
public class CsvStore implements ListOfStringArrayStore {
    private final CsvStoreProperties csvStoreProperties;
    @Override
    public void store() {
        log.info("Writing data with params: {}", csvStoreProperties);

        try (CSVWriter writer = new CSVWriter(new FileWriter(csvStoreProperties.getPathToFile(), true))) {
            writer.writeAll(csvStoreProperties.getData());
        } catch (IOException e) {
            log.error("Failed to write the file.", e);
        }
    }
}
