package com.book.village.server.domain.member.dto;

import com.book.village.server.domain.member.entity.Member;
import lombok.*;

import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.List;

public class MemberDto {
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @ToString
    @Builder
    public static class Patch{

        private Long memberId;

        @Pattern(regexp = "^[a-zA-Z0-9가-힣+_.-]+$") //영문 한글 숫자
        private String name;
        @Pattern(regexp = "^[a-zA-Z0-9가-힣+_.-]+$") //영문 한글 숫자
        private String displayName;
        private String imgUrl;

        private String address;
        private String phoneNumber;
    }
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private Long memberId;
        private String email;
        private String name;
        private String displayName;
        private String imgUrl;
        private String address;
        private String phoneNumber;
        private String memberStatus;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;

    }
}
