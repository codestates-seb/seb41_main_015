package com.book.village.server.domain.request_comment.dto;

import lombok.*;

import javax.persistence.Lob;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public class RequestCommentDto {

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class Post {
        @Lob
        @NotBlank
        private String content;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class Response {
        private Long requestCommentId;
        @NotBlank
        @Lob
        private String content;

        private String displayName;

        @Lob
        private String imgUrl;

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
        @NotBlank
        private String content;
    }


}
