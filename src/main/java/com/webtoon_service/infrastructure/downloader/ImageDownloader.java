package com.webtoon_service.infrastructure.downloader;

import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
public class ImageDownloader {
    private ImageDownloader() {}

    /**
     * Download images from a list of URLs into a local folder.
     *
     * @param imageUrls        List of URLs to download
     * @param outputDir        Target directory
     * @param baseName         Base name for image files
     * @param minSizeBytes     Minimum size (in bytes) to accept an image (e.g., 500 * 1024). Use 0 to disable.
     * @param referer          HTTP referer header to avoid 403 (e.g., "https://asuracomic.net/")
     */
    public static void downloadImages(List<String> imageUrls, Path outputDir, String baseName, int minSizeBytes, String referer) throws Exception {
        if (!Files.exists(outputDir)) {
            Files.createDirectories(outputDir);
        }

        Set<String> seen = new HashSet<>();
        int index = 1;

        for (String urlStr : imageUrls) {
            log.debug("⬇️ Attempting to download: {}", urlStr);

            if (!seen.add(urlStr)) {
                log.warn("⏩ Skipping duplicate: {}", urlStr);
                continue;
            }

            HttpURLConnection conn = null;

            try {
                URL url = new URL(urlStr);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("User-Agent", "Mozilla/5.0");
                conn.setRequestProperty("Referer", referer);
                conn.setInstanceFollowRedirects(true);
                conn.connect();

                int contentLength = conn.getContentLength();

                if (contentLength == -1) {
                    log.warn("⚠️ Unknown content length for {}. Continuing anyway...", urlStr);
                } else if (contentLength < minSizeBytes) {
                    log.warn("⏩ Skipping small image (<{} KB): {} ({} KB)", minSizeBytes / 1024, urlStr, contentLength / 1024);
                    continue;
                }

                String ext = getFileExtension(urlStr, "webp");
                String fileName = String.format("%s-%03d.%s", baseName, index++, ext);
                Path outputPath = outputDir.resolve(fileName);

                try (InputStream in = conn.getInputStream()) {
                    Files.copy(in, outputPath, StandardCopyOption.REPLACE_EXISTING);
                    log.info("✅ Downloaded: {} ({} KB)", fileName, (contentLength == -1 ? "unknown" : contentLength / 1024));
                }

            } catch (Exception e) {
                log.error("❌ Failed to download: {}", urlStr, e);
            } finally {
                if (conn != null) conn.disconnect();
            }
        }

        if (index == 1) {
            log.warn("⚠️ No images downloaded after filtering.");
        }
    }

    private static String getFileExtension(String url, String defaultExt) {
        String lower = url.toLowerCase();
        if (lower.endsWith(".jpg")) return "jpg";
        if (lower.endsWith(".jpeg")) return "jpeg";
        if (lower.endsWith(".png")) return "png";
        if (lower.endsWith(".webp")) return "webp";
        return defaultExt;
    }
}
