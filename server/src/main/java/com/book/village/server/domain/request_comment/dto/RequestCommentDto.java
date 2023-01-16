package com.book.village.server.domain.request_comment.dto;

import lombok.*;

import javax.persistence.Lob;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public class RequestCommentDto {

    @AllArgsConstructor
    @Getter
    @Setter
    @Builder
    public static class Post {
        @NotBlank
        @Lob
        private String content;
    }

    @AllArgsConstructor
    @Getter
    @Setter
    @Builder
    public static class Response {
        private long requestId;

        @NotBlank
        @Lob
        private String content;

        private String displayName;

        private LocalDateTime createdAt;

        private LocalDateTime modifiedAt;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @ToString
    public static class Patch{
        private Long requestCommentId;

        @Lob
        private String content;
    }


}
