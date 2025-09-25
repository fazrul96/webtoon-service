package com.webtoon_service.config;

import com.webtoon_service.domain.dto.WebtoonProviderDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static com.webtoon_service.constants.ApiConstant.WEBTOON.*;
import static com.webtoon_service.constants.GeneralConstants.EMPTY_STRING;
import static com.webtoon_service.constants.ModelConstants.ASURA;
import static com.webtoon_service.constants.ModelConstants.COMICK;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebtoonProviderConfig {
    private String providerId;
    private String suffix;

    private static final Map<String, WebtoonProviderDto> PROVIDERS = Map.of(
            ASURA, new WebtoonProviderDto(ASURA, ASURA_BASE_URL, ASURA_URL_PATTERN, EMPTY_STRING),
            COMICK, new WebtoonProviderDto(COMICK, COMICK_BASE_URL, COMICK_URL_PATTERN, EMPTY_STRING)
    );

    public static WebtoonProviderDto getProviderInfo(String providerId) {
        if (providerId == null) return null;
        return PROVIDERS.get(providerId.toLowerCase());
    }

    public String buildWebtoonUrl(String title) {
        WebtoonProviderDto providerInfo = getProviderInfo(providerId);
        if (providerInfo == null) {
            throw new IllegalArgumentException("Unknown providerId: " + providerId);
        }

        String encodedTitle = URLEncoder.encode(title, StandardCharsets.UTF_8);
        return providerInfo.getBaseUrl() + providerInfo.getUrlPattern()
                .replace("{suffix}", suffix != null ? suffix : EMPTY_STRING)
                .replace("{title}", encodedTitle);
    }
}
