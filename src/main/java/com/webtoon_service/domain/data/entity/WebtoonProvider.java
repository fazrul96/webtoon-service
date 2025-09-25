package com.webtoon_service.domain.data.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.webtoon_service.domain.data.BaseModel;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.util.List;

import static com.webtoon_service.constants.ModelConstants.ID;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "webtoon_providers")
@DynamicUpdate
public class WebtoonProvider extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = ID)
    @JsonProperty("id")
    private String id; // e.g., "asura", "comick"

    private String name;

    @Column(name = "base_url")
    private String baseUrl;

    @Column(name = "url_pattern")
    private String urlPattern;

    @OneToMany(mappedBy = "provider", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WebtoonSource> sources;
}

