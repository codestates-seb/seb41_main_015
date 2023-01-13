package com.book.village.server.domain.borrowcomment.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
public class BorrowCommentDto {

    @AllArgsConstructor
    @Setter
    @Getter
    @Builder
    public static class Post {
        private String content;
    }

    @AllArgsConstructor
    @Setter
    @Getter
    @Builder
    public static class Patch {
        private Long borrowCommentId;
        private String content;
    }


    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long borrowCommentId;
        private Long memberId;
        private Long borrowId;
        private String content;
        private String displayName;
        private LocalDateTime createdAt;
        private LocalDateTime modifedAt;
    }

}
