package com.book.village.server.domain.borrowcomment.dto;

import lombok.*;

import javax.persistence.Lob;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
public class BorrowCommentDto {

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @ToString
    public static class Post {
        @Lob
        private String content;

    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @ToString
    public static class Patch {
        private Long borrowCommentId;
        @Lob
        private String content;
    }


    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @ToString
    public static class Response {
        private Long borrowCommentId;
        @Lob
        private String content;
        private String displayName;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
    }

}
