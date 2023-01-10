package com.book.village.server.domain.member.dto;

import lombok.*;

import javax.validation.constraints.Pattern;
import java.util.List;

public class MemberDto {
    @Getter
    @ToString
    @Builder
    public static class Patch{
        @Pattern(regexp = "^[a-zA-Z0-9가-힣+_.-]+$") //영문 한글 숫자
        private String name;
        @Pattern(regexp = "^[a-zA-Z0-9가-힣+_.-]+$") //영문 한글 숫자
        private String displayName;

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
        private String phoneNumber;

    }
}
