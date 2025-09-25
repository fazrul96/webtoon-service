package com.webtoon_service.domain.data.repository;

import com.webtoon_service.domain.data.entity.WebtoonChapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WebtoonChapterRepository extends JpaRepository<WebtoonChapter, Long> {
    List<WebtoonChapter> findByWebtoonSourceIdOrderByChapterNumberAsc(Long sourceId);

    Optional<WebtoonChapter> findByWebtoonSourceIdAndChapterNumber(Long sourceId, String chapterNumber);

    @Query("SELECT c " +
            "FROM WebtoonChapter c " +
            "WHERE c.webtoonSource.webtoon.id = :webtoonId " +
            "AND c.chapterNumber = :chapter")
    Optional<WebtoonChapter> findChapterByWebtoonIdAndNumber(
            @Param("webtoonId") Long webtoonId,
            @Param("chapter") String chapter
    );
}
