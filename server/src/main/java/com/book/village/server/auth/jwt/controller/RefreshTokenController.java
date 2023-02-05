package com.book.village.server.auth.jwt.controller;

import com.book.village.server.auth.jwt.tokenizer.JwtTokenizer;
import com.book.village.server.auth.jwt.service.RefreshTokenService;
import com.book.village.server.global.response.MessageResponseDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
public class RefreshTokenController {
    private final JwtTokenizer jwtTokenizer;
    private final RefreshTokenService tokenService;
    @Getter
    @Value("${jwt.key.secret}")
    private String secretKey;


    @PostMapping("/auth/token")
    public ResponseEntity getAccessToken(HttpServletRequest request, HttpServletResponse response) {

        String token = request.getHeader("Authorization");

        String encodedBase64SecretKey = jwtTokenizer.encodeBase64SecretKey(secretKey);

        String email = jwtTokenizer.getEmailFromToken(token, encodedBase64SecretKey);

        tokenService.verifyToken(email, token);
        String accessToken= tokenService.getAccessToken(email);

        log.info("accessToken = {}", accessToken);
        response.addHeader("Authorization", accessToken);

        return ResponseEntity.ok(new MessageResponseDto("access token created!"));
    }
}
