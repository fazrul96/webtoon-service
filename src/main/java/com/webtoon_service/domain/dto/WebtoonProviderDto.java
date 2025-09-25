package com.webtoon_service.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class WebtoonProviderDto {
    private String id;
    private String baseUrl;
    private String urlPattern;
    private String suffix;
}
