package com.webtoon_service.infrastructure.downloader;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Slf4j
public class ZipCompressor {

    public static void zipFolder(Path sourceDir, Path outputZipPath) throws IOException {
        if (!Files.exists(sourceDir) || !Files.isDirectory(sourceDir)) {
            throw new IllegalArgumentException("Source directory does not exist: " + sourceDir);
        }

        try (ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(outputZipPath))) {
            Files.walk(sourceDir)
                    .filter(path -> !Files.isDirectory(path))
                    .forEach(file -> {
                        ZipEntry zipEntry = new ZipEntry(sourceDir.relativize(file).toString());
                        try {
                            zos.putNextEntry(zipEntry);
                            Files.copy(file, zos);
                            zos.closeEntry();
                            log.debug("Zipped: {}", zipEntry.getName());
                        } catch (IOException e) {
                            throw new RuntimeException("Failed to zip file: " + file, e);
                        }
                    });
        }

        log.info("✅ Created zip: {}", outputZipPath.toAbsolutePath());
    }
}
