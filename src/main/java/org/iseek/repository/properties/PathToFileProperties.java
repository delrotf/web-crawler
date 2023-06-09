package org.iseek.repository.properties;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PathToFileProperties {
    String outputDirectory;
    String filenamePrefix;
}
