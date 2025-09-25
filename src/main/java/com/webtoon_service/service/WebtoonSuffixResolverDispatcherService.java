package com.webtoon_service.service;

import com.webtoon_service.domain.data.WebtoonSuffixResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class WebtoonSuffixResolverDispatcherService {

    private final List<WebtoonSuffixResolver> resolvers;

    public Map<String, String> resolve(String provider, String title, boolean headlessMode) {
        return resolvers.stream()
                .filter(resolver -> resolver.supports(provider))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unsupported provider: " + provider))
                .resolve(provider, title, headlessMode);
    }
}