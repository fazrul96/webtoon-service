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
@Table(name = "webtoon_sources", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"webtoon_id", "provider_id"})
})
@DynamicUpdate
public class WebtoonSource extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = ID)
    @JsonProperty("id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "webtoon_id", nullable = false)
    private Webtoon webtoon;

    @ManyToOne
    @JoinColumn(name = "provider_id", nullable = false)
    private WebtoonProvider provider;

    private String suffix;
    private String url;

    @OneToMany(mappedBy = "webtoonSource", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WebtoonChapter> chapters;
}
