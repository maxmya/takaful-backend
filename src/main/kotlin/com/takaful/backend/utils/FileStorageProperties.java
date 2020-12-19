package com.takaful.backend.utils;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * author maxmya
 */
@Configuration
@ConfigurationProperties(prefix = "file")
public class FileStorageProperties {

    private String directory;

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }
}
