package com.webtoon_service.domain.data.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.webtoon_service.domain.data.BaseModel;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;

import static com.webtoon_service.constants.ModelConstants.ID;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "webtoon_chapters", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"webtoon_source_id", "chapter_number"})
})
@DynamicUpdate
public class WebtoonChapter extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = ID)
    @JsonProperty("id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "webtoon_source_id", nullable = false)
    private WebtoonSource webtoonSource;

    @Column(name = "chapter_number")
    private String chapterNumber;

    private String title;

    @Column(name = "s3_zip_url")
    private String s3ZipUrl;

    @Column(name = "release_date")
    private LocalDate releaseDate;
}

