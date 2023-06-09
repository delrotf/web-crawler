package org.iseek.repository.properties;

import lombok.*;

import java.util.List;

@Value
@Builder
public class CsvStoreProperties {
    String pathToFile;
    List<String[]> data;
}
