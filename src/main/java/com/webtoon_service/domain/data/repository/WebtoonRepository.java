package com.webtoon_service.domain.data.repository;

import com.webtoon_service.domain.data.entity.Webtoon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WebtoonRepository extends JpaRepository<Webtoon, Long> {
    Optional<Webtoon> findByAlias(String alias);
    Optional<Webtoon> findByTitle(String title);

    List<Webtoon> findByTitleContainingIgnoreCase(String title);

    boolean existsByAlias(String alias);
}
