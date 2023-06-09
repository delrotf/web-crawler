package org.iseek.repository;

import org.iseek.repository.impl.CsvStore;
import org.iseek.repository.properties.CsvStoreProperties;
import org.iseek.repository.properties.PathToFileProperties;
import org.iseek.util.PathToFileUtil;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ListOfStringArrayStoreTest {

    @Test
    void store() {
        List<String[]> data = new ArrayList<>();
        data.add(new String[]{"url", "title", "meta"});
        data.add(new String[]{"url2", "title2", "meta2"});

        String outputDirectory = PathToFileUtil.getDefaultOutputDirectory();
        String fileNamePrefix = PathToFileUtil.getDefaultFilenamePrefix();

        String pathToFile = PathToFileUtil.assemblePathToFile(PathToFileProperties.builder()
                .outputDirectory(outputDirectory)
                .filenamePrefix(fileNamePrefix)
                .build());

        CsvStore csvWriter = new CsvStore(CsvStoreProperties.builder()
                .data(data)
                .pathToFile(pathToFile)
                .build());
        csvWriter.store();
        assertTrue(new File(pathToFile).exists(), "Output file should exist");
    }
}