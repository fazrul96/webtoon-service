package com.webtoon_service.domain.data.repository;

import com.webtoon_service.domain.data.entity.WebtoonProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WebtoonProviderRepository extends JpaRepository<WebtoonProvider, String> {
    Optional<WebtoonProvider> findByNameIgnoreCase(String name);
}