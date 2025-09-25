package com.webtoon_service.service;

import com.webtoon_service.domain.data.entity.Webtoon;
import com.webtoon_service.domain.data.entity.WebtoonChapter;
import com.webtoon_service.domain.data.entity.WebtoonProvider;
import com.webtoon_service.domain.data.entity.WebtoonSource;
import com.webtoon_service.domain.data.repository.WebtoonChapterRepository;
import com.webtoon_service.domain.data.repository.WebtoonProviderRepository;
import com.webtoon_service.domain.data.repository.WebtoonRepository;
import com.webtoon_service.domain.data.repository.WebtoonSourceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class WebtoonService {
    private final WebtoonRepository webtoonRepository;
    private final WebtoonSourceRepository sourceRepository;
    private final WebtoonChapterRepository chapterRepository;
    private final WebtoonProviderRepository providerRepository;

    public Webtoon getOrCreateWebtoon(String title, String alias) {
        return webtoonRepository.findByTitle(title)
                .or(() -> webtoonRepository.findByAlias(alias))
                .orElseGet(() -> {
                    Webtoon webtoon = Webtoon.builder()
                            .title(title)
                            .alias(alias)
                            .chapterCount(0)
                            .build();
                    return webtoonRepository.save(webtoon);
                });
    }


    public WebtoonSource getOrCreateSource(Webtoon webtoon, String providerId, String suffix) {
        WebtoonProvider provider = providerRepository.findById(providerId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid provider: " + providerId));

        return sourceRepository.findByWebtoonAndProvider(webtoon, provider)
                .orElseGet(() -> sourceRepository.save(WebtoonSource.builder()
                        .webtoon(webtoon)
                        .provider(provider)
                        .suffix(suffix)
                        .build()));
    }

    public WebtoonChapter saveChapter(WebtoonSource source, String chapter, String zipUrl) {
        return chapterRepository.findByWebtoonSourceIdAndChapterNumber(source.getId(), chapter)
                .orElseGet(() -> chapterRepository.save(WebtoonChapter.builder()
                        .webtoonSource(source)
                        .chapterNumber(chapter)
//                        .zipUrl(zipUrl)
                        .build()));
    }

    public List<WebtoonChapter> getChapters(Long webtoonId, String providerId) {
        Webtoon webtoon = webtoonRepository.findById(webtoonId)
                .orElseThrow(() -> new IllegalArgumentException("Webtoon not found"));

        WebtoonProvider provider = providerRepository.findById(providerId)
                .orElseThrow(() -> new IllegalArgumentException("Provider not found"));

        WebtoonSource source = sourceRepository.findByWebtoonAndProvider(webtoon, provider)
                .orElseThrow(() -> new IllegalArgumentException("Source not found"));

        return chapterRepository.findByWebtoonSourceIdOrderByChapterNumberAsc(source.getId());
    }
}
