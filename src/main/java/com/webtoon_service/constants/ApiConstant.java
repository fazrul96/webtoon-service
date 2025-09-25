package com.webtoon_service.constants;

import static com.webtoon_service.constants.GeneralConstants.SLASH;

public final class ApiConstant {
    private ApiConstant() {}

    public static final String WEBTOONS = "webtoons";
    public static final String WEBTOONS_TITLE = "webtoons/{title}";
    public static final String WEBTOONS_TITLE_CHAPTERS = "webtoons/{title}/chapters";
    public static final String WEBTOONS_SYNC = "webtoon-sync";
    public static final String DOWNLOAD_WEBTOON = "download-webtoon";
    public static final String DOWNLOAD_WEBTOON_CHAPTERS = "download-webtoon-chapters";
    public static final String UPLOAD_WEBTOON = "upload-webtoon";
    public static final String LATEST_CHAPTER_WEBTOON = "latest-chapter-webtoon";
    public static final String ID_PATH = "/{id}";

    public static class AUTH0 {
        public static final String AUTHORIZE_URL = "authorize";
        public static final String DEVICE_AUTHORIZATION_URL = "oauth/device/code";
        public static final String TOKEN_URL = "oauth/token";
        public static final String USER_INFO_URL = "userinfo";
        public static final String OPENID_CONFIG = ".well-known/openid-configuration";
        public static final String JWKS_JSON = ".well-known/jwks.json";
    }

    public static class WEBTOON {
        public static final String ASURA_BASE_URL = "https://asuracomic.net";
        public static final String COMICK_BASE_URL = "https://comick.art";
        public static final String HIVETOON_BASE_URL = "https://hivetoon.com";
        public static final String VORTEX_BASE_URL = "https://vortexscans.org";
        public static final String MANGATOWN_BASE_URL = "https://www.mangatown.com";
        public static final String ROLIOSCAN_BASE_URL = "https://roliascan.com";

        public static final String ASURA_URL_PATTERN = "/series/{title}-{suffix}";
        public static final String COMICK_URL_PATTERN = "/comic/{suffix}-{title}";

//        HIVETOON_CHAPTER_SUFFIX = "-chapter-{chapter}/"
//        ASURA_CHAPTER_SUFFIX = "{suffix}/chapter/{chapter}/"
//        VORTEX_CHAPTER_SUFFIX = "/chapter-{chapter}"
//        MANGATOWN_CHAPTER_SUFFIX = "/c{chapter}"
//        ROLIOSCAN_CHAPTER_SUFFIX = "/chapter-{chapter}"

        public static final String ASURA_SEARCH_PATH = SLASH + "series";
        public static final String COMICK_SEARCH_PATH = SLASH + "api/search";

        public static final String ASURA_SEARCH_QUERY = "?page=%d&name=%s";

        public static final String UPLOAD_WEBTOON_CHAPTER = "upload-webtoon-chapter";
        public static final String FETCH_SUFFIX = "fetch-suffix";

        public static final String UPLOAD_FILES_S3 = SLASH + "s3/uploadFiles";
        public static final String S3_WEBTOON_BUCKET = "webtoons-content" + SLASH;

        public static String getAsuraSearchUrl(int page, String name) {
            return String.format(ASURA_BASE_URL + ASURA_SEARCH_PATH + ASURA_SEARCH_QUERY, page, name);
        }

        public static String getAsuraSearchUrl(String name) {
            return getAsuraSearchUrl(1, name);
        }

        public static String getComickSearchUrl() {
            return COMICK_BASE_URL + COMICK_SEARCH_PATH;
        }
    }
}