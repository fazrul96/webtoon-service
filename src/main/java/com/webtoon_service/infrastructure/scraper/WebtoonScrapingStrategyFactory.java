package com.webtoon_service.infrastructure.scraper;

import com.webtoon_service.infrastructure.scraper.provider.WebtoonScrapingStrategyAsura;
import com.webtoon_service.infrastructure.scraper.provider.WebtoonScrapingStrategyComick;

import static com.webtoon_service.constants.ModelConstants.ASURA;
import static com.webtoon_service.constants.ModelConstants.COMICK;

public class WebtoonScrapingStrategyFactory {
    /**
     * Factory method to get the appropriate scraping strategy based on the provider's ID.
     * @param providerId The provider's unique ID.
     * @return The appropriate WebtoonScrapingStrategy for the provider.
     */
    public static WebtoonScrapingStrategy getStrategy(String providerId) {
        switch (providerId.toLowerCase()) {
            case COMICK:
                return new WebtoonScrapingStrategyComick();
            case ASURA:
                return new WebtoonScrapingStrategyAsura();
            // Add other cases as you add more providers
            default:
                throw new IllegalArgumentException("No scraping strategy found for provider: " + providerId);
        }
    }
}
