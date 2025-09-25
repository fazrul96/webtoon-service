package com.webtoon_service.domain.data;

import java.util.Map;

public interface WebtoonSuffixResolver {
    /**
     * Checks whether the resolver supports the given provider.
     * @param provider The provider to check.
     * @return true if this resolver supports the provider, false otherwise.
     */
    boolean supports(String provider);

    /**
     * Resolves the suffix based on the title for a given provider.
     * This method will return the title and suffix in a Map.
     * @param provider The provider's name.
     * @param title The webtoon title.
     * @param headlessMode Flag indicating whether the scraper should run in headless mode.
     * @return A map containing the resolved title and suffix.
     */
    Map<String, String> resolve(String provider, String title, boolean headlessMode);
}


