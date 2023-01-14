package com.book.village.server.domain.community_comment.dto;

import lombok.*;

import javax.persistence.Lob;
import java.time.LocalDateTime;

public class CommunityCommentDto {
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @ToString
    @Builder
    public static class Post{
        @Lob
        private String content;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @ToString
    @Builder
    public static class Patch{
        private Long communityCommentId;

        @Lob
        private String content;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @ToString
    @Builder
    public static class Response{
        private Long communityCommentId;
        @Lob
        private String content;
        private String displayName;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
    }

}
