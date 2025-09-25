package com.webtoon_service.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchApiResponse {
    private int current_page;
    private List<Result> data;

    @Data
    public static class Result {
        private long id;
        private String hid;
        private String slug;
        private String title;
        private int rating_count;
        private String bayesian_rating;
    }
}
