package com.book.village.server.domain.community.dto;

import com.book.village.server.domain.community_comment.dto.CommunityCommentDto;
import lombok.*;

import javax.persistence.Lob;
import java.time.LocalDateTime;
import java.util.List;

public class CommunityDto {
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @ToString
    public static class Post{
        private String type;
        private String title;
        @Lob
        private String content;
        private String displayName;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @ToString
    public static class Patch{
        private Long communityId;
        private String type;
        private String title;
        @Lob
        private String content;
        private String displayName;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @ToString
    public static class Response{
        private Long communityId;
        private String type;
        private String title;
        @Lob
        private String content;
        private String displayName;
        private String imgUrl;
        private List<CommunityCommentDto.Response> communityComments;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
    }
}
