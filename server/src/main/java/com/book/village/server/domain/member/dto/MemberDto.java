package com.book.village.server.domain.member.dto;

import com.book.village.server.domain.member.entity.Member;
import lombok.*;

import javax.persistence.Lob;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
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
        @Size(min = 2, max = 20, message = "닉네임은 2자 이상 20자 이하로 입력해주세요.")
        private String displayName;
        @Lob
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
        @Lob
        private String imgUrl;
        private String address;
        private String phoneNumber;
        private String memberStatus;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;

    }
}
