package com.book.village.server.auth.jwt.dto;

import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RefreshTokenDto {
    private String refreshToken;
}
