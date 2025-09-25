package com.webtoon_service.constants;

import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public final class SecurityConstants {
    public static final String SAMEORIGIN = "SAMEORIGIN";
    public static final List<String> ALLOWED_ORIGINS = CORS.ALLOWED_ORIGINS;
    public static final List<String> ALLOWED_METHODS = CORS.ALLOWED_METHODS;
    public static final List<String> ALLOWED_HEADERS = CORS.ALLOWED_HEADERS;
    public static final String[] ADDITIONAL_PATHS;

    public static class CORS {
        public static final String AUTHORIZATION = "Authorization";
        public static final String CONTENT_TYPE = "Content-Type";
        public static final String CACHE_CONTROL = "Cache-Control";
        public static final String USER_ID = "UserId";

        public static final String GET = "GET";
        public static final String POST = "POST";
        public static final String PUT = "PUT";
        public static final String PATCH = "PATCH";
        public static final String DELETE = "DELETE";
        public static final String OPTIONS = "OPTIONS";

        public static final List<String> ALLOWED_ORIGINS = List.of(
                "http://localhost:3000",
                "http://localhost:5173",
                "http://localhost:4200",
                "https://portfolio-portal.mfzrl.cyou",
                "https://mosque-portal.mfzrl.cyou",
                "https://insurance-portal.mfzrl.cyou"
        );

        public static final List<String> ALLOWED_METHODS = List.of(
                GET, POST, PUT, PATCH, DELETE, OPTIONS
        );

        public static final List<String> ALLOWED_HEADERS = List.of(
                AUTHORIZATION, CONTENT_TYPE, CACHE_CONTROL, USER_ID
        );
    }

    public static class SWAGGER {
        public static final String[] PATHS = {
                "/swagger-ui.html",
                "/swagger-ui/**",
                "/v3/api-docs/**",
                "/api-docs/**",
                "/swagger-resources/**"
        };
    }

    static {
        List<String> allPaths = new ArrayList<>();
        allPaths.addAll(List.of(SWAGGER.PATHS));
        ADDITIONAL_PATHS = allPaths.toArray(new String[0]);
    }
}