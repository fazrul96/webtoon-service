package com.webtoon_service.component;

import com.webtoon_service.constants.ApiConstant;
import com.webtoon_service.domain.data.WebtoonSuffixResolver;
import com.webtoon_service.domain.dto.response.SearchApiResponse;
import com.webtoon_service.util.WebtoonSlugUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.Map;

import static com.webtoon_service.constants.ModelConstants.COMICK;
import static com.webtoon_service.util.WebtoonSlugUtil.isTitleMatch;

@Component
public class SuffixResolverComick implements WebtoonSuffixResolver {
    private final WebClient webClient;

    public SuffixResolverComick() {
        String searchUrl = ApiConstant.WEBTOON.getComickSearchUrl();
        this.webClient = WebClient.builder().baseUrl(searchUrl).build();
    }

    @Override
    public boolean supports(String provider) {
        return COMICK.equalsIgnoreCase(provider);
    }

    @Override
    public Map<String, String> resolve(String provider, String title, boolean headlessMode) {
        String partialTitle = getPartialTitle(title);

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("q", partialTitle)
                        .queryParam("limit", 10)
                        .build())
                .retrieve()
                .bodyToMono(SearchApiResponse.class)
                .flatMapMany(response -> {
                    if (response.getData() == null) {
                        return Flux.empty();
                    }
                    return Flux.fromIterable(response.getData());
                })
                .filter(result -> isTitleMatch(result.getTitle(), title))
                .next()
                .map(result -> {
                    String slug = result.getSlug();
                    String suffix = WebtoonSlugUtil.extractComickSuffix(slug);
                    String cleanTitle = WebtoonSlugUtil.extractComickTitle(slug);
                    return Map.of(
                            "provider", provider,
                            "title", cleanTitle,
                            "suffix", suffix
                    );
                })
                .blockOptional()
                .orElseThrow(() -> new RuntimeException("❌ No matching title found for: " + title));
    }

    private String getPartialTitle(String title) {
        return title.length() > 4 ? title.substring(0, 4) : title;
    }
}
