package com.webtoon_service.properties;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
@Data
@ConfigurationProperties("app")
public class AppProperties {
    private String basePath;
    private String baseFlaskPath;
    private String apiPath;
    private String privateApiPath;
    private String publicApiPath;
}
