package com.book.village.server.auth.jwt.service;

import com.book.village.server.auth.jwt.entity.RefreshToken;
import com.book.village.server.auth.jwt.repository.RefreshTokenRepository;
import com.book.village.server.domain.member.repository.MemberRepository;
import com.book.village.server.domain.member.service.MemberService;
import com.book.village.server.global.exception.CustomLogicException;
import com.book.village.server.global.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberService memberService;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, MemberService memberService) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.memberService = memberService;
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

    public void deleteToken(String memberId) {
        RefreshToken findRefreshToken = findVerifiedToken(memberId);
        refreshTokenRepository.delete(findRefreshToken);
    }
}
