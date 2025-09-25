package com.webtoon_service.constants;

public final class WebtoonConstants {
    private WebtoonConstants() {}
    public static final String WEBTOON_CHAPTER_FILENAME_FORMAT = "%s-chapter-%s";
    public static final String TEMP_DIR_PREFIX = "webtoon-";
    public static final String IMAGES_DIR_NAME = "images";

    public static final int MIN_IMAGE_SIZE_BYTES_COMICK = 500 * 1024; // 500 KB
    public static final int MIN_IMAGE_SIZE_BYTES_ASURA = 0;
}
