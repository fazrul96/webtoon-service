package com.webtoon_service.domain.data.repository;

import com.webtoon_service.domain.data.entity.Webtoon;
import com.webtoon_service.domain.data.entity.WebtoonProvider;
import com.webtoon_service.domain.data.entity.WebtoonSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WebtoonSourceRepository extends JpaRepository<WebtoonSource, Long> {
    Optional<WebtoonSource> findByWebtoonAndProvider(Webtoon webtoon, WebtoonProvider provider);

    List<WebtoonSource> findByProviderId(String providerId);
}
