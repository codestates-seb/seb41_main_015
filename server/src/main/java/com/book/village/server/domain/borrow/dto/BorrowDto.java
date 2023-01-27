package com.book.village.server.domain.borrow.dto;

import com.book.village.server.domain.borrowcomment.dto.BorrowCommentDto;
import lombok.*;

import javax.persistence.Lob;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

public class BorrowDto {

    @AllArgsConstructor
    @Setter
    @Getter
    @Builder
    public static class Post {
        @NotBlank
        private String title;       // 나눔 제목
        @NotBlank
        @Lob
        private String content;     // 나눔 본문
        @NotBlank
        private String bookTitle;   // 나눔 책 제목
        @NotBlank
        private String author;      // 나눔 책 작가
        @NotBlank
        private String publisher;   // 나눔 책 출판사

        @NotBlank
        @Lob
        private String thumbnail;

        private String talkUrl;     // 오픈 챗 링크
    }

    @AllArgsConstructor
    @Setter
    @Getter
    @Builder
    public static class Patch {
        @Setter
        private Long borrowId;  // 나눔 게시판 식별자

        @NotBlank
        private String title;   // 나눔글 제목

        @Lob
        @NotBlank
        private String content; // 나눔글 본문

        private String bookTitle;   // 나눔 책 제목

        private String author;      // 나눔 책 저자

        private String publisher;   // 나눔 책 출판사

        @Lob
        private String thumbnail;

        private String talkUrl;     // 톡 링크

        private Boolean borrowWhthr; // 나눔 가능여부
        private Long view;      // 조회 수
    }


    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long borrowId;  //  식별자

        private String title;   //  나눔글 제목
        @Lob
        private String content; //  나눔글 본문
        private String bookTitle;   // 나눔 책 제목
        private String author;      // 나눔 책 저자
        private String publisher;   // 나눔 책 출판사
        @Lob
        private String thumbnail;
        private String displayName; // 회원 닉네임
        @Lob
        private String imgUrl;
        private String talkUrl;     // 톡링크

        private Boolean borrowWhthr; // 나눔 가능여부
        private Long view;      // 조회 수

        private List<BorrowCommentDto.Response> borrowComments; // 나눔 댓글리스트.

        private LocalDateTime createdAt;     // 나눔글 생성 일자

        private LocalDateTime modifiedAt;   // 나눔글 최근 수정 일자
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

        private Long count;
    }

}
