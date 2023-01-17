package com.book.village.server.domain.book.dto;

import com.book.village.server.domain.community_comment.dto.CommunityCommentDto;
import lombok.*;

import java.time.LocalDateTime;

public class BookDto {
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @ToString
    public static class Patch{
        private String isbn;
        private String bookTitle;
        private String author;
        private String publisher;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @ToString
    public static class Response{
        private Long bookId;
        private String isbn;
        private String bookTitle;
        private String author;
        private String publisher;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
    }
}
