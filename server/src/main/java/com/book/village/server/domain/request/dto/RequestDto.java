package com.book.village.server.domain.request.dto;

import com.book.village.server.domain.community_comment.dto.CommunityCommentDto;
import com.book.village.server.domain.request_comment.dto.RequestCommentDto;
import com.book.village.server.domain.request_comment.entity.RequestComment;
import lombok.*;

import javax.persistence.Lob;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

public class RequestDto {

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class Post {

        private String talkUrl;

        @NotBlank
        private String title;

        @NotBlank
        @Lob
        private String content;

        @NotBlank
        private String bookTitle;

        @NotBlank
        private String author;

        @NotBlank
        private String publisher;


    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class Response {
        private long requestId;

        private String talkUrl;

        @NotBlank
        private String title;

        @NotBlank
        @Lob
        private String content;

        @NotBlank
        private String bookTitle;

        @NotBlank
        private String author;

        @NotBlank
        private String publisher;

        private String displayName;

        private List<RequestCommentDto.Response> requestComments;

        private LocalDateTime createdAt;

        private LocalDateTime modifiedAt;

    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class Patch {

        private long requestId;

        private String talkUrl;

        private String title;

        @Lob
        private String content;

        private String bookTitle;

        private String author;

        private String publisher;
    }
}
