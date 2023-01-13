package com.book.village.server.domain.community.dto;

import lombok.*;

import javax.persistence.Lob;
import java.time.LocalDateTime;

public class CommunityDto {
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @ToString
    @Builder
    public static class Post{
        private String title;
        @Lob
        private String content;
        private String displayName;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @ToString
    @Builder
    public static class Patch{
        private Long communityId;
        private String title;
        @Lob
        private String content;
        private String displayName;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @ToString
    @Builder
    public static class Response{
        private Long communityId;
        private String title;
        @Lob
        private String content;
        private String displayName;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
    }
}
