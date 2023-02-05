package com.book.village.server.auth.jwt.service;

import com.book.village.server.auth.jwt.tokenizer.JwtTokenizer;
import com.book.village.server.auth.jwt.entity.RefreshToken;
import com.book.village.server.auth.jwt.repository.RefreshTokenRepository;
import com.book.village.server.auth.utils.CustomAuthorityUtils;
import com.book.village.server.domain.member.entity.Member;
import com.book.village.server.domain.member.service.MemberService;
import com.book.village.server.global.exception.CustomLogicException;
import com.book.village.server.global.exception.ExceptionCode;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberService memberService;
    private final CustomAuthorityUtils authorityUtils;
    private final JwtTokenizer jwtTokenizer;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, MemberService memberService, CustomAuthorityUtils authorityUtils, JwtTokenizer jwtTokenizer) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.memberService = memberService;
        this.authorityUtils = authorityUtils;
        this.jwtTokenizer = jwtTokenizer;
    }

    public void verifyToken(String email, String token) {
        RefreshToken refreshToken = findVerifiedToken(email);
        if (!refreshToken.getRefreshToken().equals(token)) throw new CustomLogicException(ExceptionCode.TOKEN_INVALID);
    }

    public RefreshToken findVerifiedToken(String email) {
        Optional<RefreshToken> optional =
                refreshTokenRepository.findByMember(memberService.findMember(email));
        RefreshToken findRefreshToken = optional.orElseThrow(() -> new CustomLogicException(ExceptionCode.REFRESH_TOKEN_NOT_FOUND));
        return findRefreshToken;
    }
    public String getAccessToken(String email){
        Member member = memberService.findMember(email);
        List<String> authorities = authorityUtils.createRoles(email);

        Map<String, Object> claims = new HashMap<>();
        claims.put("username", email);
        claims.put("roles", authorities);

        String subject = email;
        Date expiration = jwtTokenizer.getTokenExpiration(jwtTokenizer.getAccessTokenExpirationMinutes());

        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());

        String accessToken = jwtTokenizer.generateAccessToken(claims, subject, expiration, base64EncodedSecretKey);
        return accessToken;
    }

    public void deleteToken(String memberId) {
        RefreshToken findRefreshToken = findVerifiedToken(memberId);
        refreshTokenRepository.delete(findRefreshToken);
    }
}
