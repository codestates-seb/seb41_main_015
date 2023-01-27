package com.book.village.server.domain.rate.dto;

import lombok.*;
import org.hibernate.validator.constraints.Range;

import javax.persistence.Lob;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public class RateDto {
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @ToString
    public static class Post{
        @Range(min = 1,max = 5)
        private Long rating;
        @Lob
        @NotBlank
        private String content;
        @Lob
        private String thumbnail;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @ToString
    public static class Patch {
        private Long rateId;
        @Range(min = 1,max = 5)
        private Long rating;
        private String displayName;
        @Lob
        @NotBlank
        private String content;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @ToString
    public static class Response{
        private Long rateId;
        @Range(min = 1,max = 5)
        @NotBlank
        private Long rating;
        private String displayName;
        @Lob
        private String imgUrl;
        @Lob
        @NotBlank
        private String content;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
    }

}
