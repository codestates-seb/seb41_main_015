package com.book.village.server.domain.request.dto;

import lombok.*;

import javax.persistence.Lob;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public class RequestDto {

    @AllArgsConstructor
    @Getter
    @Setter
    @Builder
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
    @Getter
    @Setter
    @Builder
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

        private LocalDateTime createdAt;

        private LocalDateTime modifiedAt;

    }

    @AllArgsConstructor
    @Getter
    @Setter
    @Builder
    public static class Patch {

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
    }
}
