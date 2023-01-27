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

        @Lob
        private String thumbnail;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class Response {
        private Long requestId;

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

        @Lob
        private String thumbnail;

        private String displayName;

        private Long view;
        @Lob
        private String imgUrl;

        private List<RequestCommentDto.Response> requestComments;

        private LocalDateTime createdAt;

        private LocalDateTime modifiedAt;

    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class Patch {

        private Long requestId;

        private String talkUrl;
        @NotBlank
        private String title;

        private Long view;

        @Lob
        @NotBlank
        private String content;

        private String bookTitle;

        private String author;

        private String publisher;

        @Lob
        private String thumbnail;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class rankResponse {
        @Lob
        private String bookTitle;

        private String author;

        private String publisher;

        @Lob
        private String thumbnail;

        private Long count;


    }

}
