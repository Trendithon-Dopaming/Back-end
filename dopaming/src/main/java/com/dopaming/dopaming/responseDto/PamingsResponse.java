package com.dopaming.dopaming.responseDto;

import com.dopaming.dopaming.domain.Category;
import com.dopaming.dopaming.domain.Steps;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class PamingsResponse {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetOngoingPamingListDTO {
        private List<PamingsResponse.GetOngoingPamingDTO> pamings;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetOngoingPamingDTO {

        private Long paming_id;

        private String paming_title;

        private Category category;

        private String photo_url;

        private String start_date;

        private String end_date;

        private Long remaining_period;

        private Long cleared_step;

        private Long unclear_step;

        private List<PamingsResponse.GetStepDTO> steps;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetStepDTO {

        private int step;

        private String content;

        private boolean success;
    }
}
