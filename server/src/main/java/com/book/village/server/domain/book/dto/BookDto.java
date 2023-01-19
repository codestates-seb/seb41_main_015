package com.book.village.server.domain.book.dto;


import com.book.village.server.domain.rate.dto.RateDto;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

public class BookDto {
    @AllArgsConstructor
    @NoArgsConstructor
    @Setter
    @Getter
    @ToString
    public static class Patch{
        private Long bookId;
        private String isbn;
        private String bookTitle;
        private String author;
        private String publisher;
        private String thumbnail;
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
        private String thumbnail;
        private Double avgRate;
        private List<RateDto.Response> rates;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
    }
}
