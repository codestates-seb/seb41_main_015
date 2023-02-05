package com.book.village.server.domain.member.service;

import com.book.village.server.auth.jwt.tokenizer.JwtTokenizer;
import com.book.village.server.auth.jwt.repository.RefreshTokenRepository;
import com.book.village.server.auth.utils.CustomAuthorityUtils;
import com.book.village.server.domain.member.entity.Member;
import com.book.village.server.domain.member.repository.MemberRepository;
import com.book.village.server.global.exception.CustomLogicException;
import com.book.village.server.global.exception.ExceptionCode;
import com.book.village.server.global.utils.CustomBeanUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;
    private final CustomBeanUtils<Member> customBeanUtils;
    private final CustomAuthorityUtils authorityUtils;
    private final RedisTemplate redisTemplate;
    private final JwtTokenizer jwtTokenizer;
    private final RefreshTokenRepository tokenRepository;

    public MemberService(MemberRepository memberRepository, CustomBeanUtils<Member> customBeanUtils, CustomAuthorityUtils authorityUtils, RedisTemplate redisTemplate, JwtTokenizer jwtTokenizer, RefreshTokenRepository tokenRepository) {
        this.memberRepository = memberRepository;
        this.customBeanUtils = customBeanUtils;
        this.authorityUtils = authorityUtils;
        this.redisTemplate = redisTemplate;
        this.jwtTokenizer = jwtTokenizer;
        this.tokenRepository = tokenRepository;
    }

    public Member createMember(Member member) {
        findByEmail(member.getEmail()).ifPresent(m -> {
            throw new CustomLogicException(ExceptionCode.MEMBER_DUPLICATE);
        });

        member.setMemberStatus(Member.MemberStatus.MEMBER_ACTIVE);
        member.setRoles(authorityUtils.createRoles(member.getEmail()));
        return memberRepository.save(member);
    }

    private Optional<Member> findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    public Member findMember(String email) {
        return findByEmail(email).orElseThrow(() -> new CustomLogicException(ExceptionCode.MEMBER_NOT_FOUND));
    }

    public Member findMember(Long id) {
        return memberRepository.findById(id).orElseThrow(() -> new CustomLogicException(ExceptionCode.MEMBER_NOT_FOUND));
    }

    public Member updateMember(Member member, Member patchMember) {
        if(patchMember.getDisplayName()!=null) {
            memberRepository.findByDisplayName(patchMember.getDisplayName()).ifPresent( m -> {
                throw new CustomLogicException(ExceptionCode.MEMBER_DUPLICATE);
                    }
            );
        }
        return customBeanUtils.copyNonNullProperties(patchMember, member);
    }
    public void quitMember(String email){
        Member findMember = findMember(email);
        findMember.setMemberStatus(Member.MemberStatus.MEMBER_QUIT);
    }

    public void registerLogoutToken(String jws, String pemail) {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        Jws<Claims> jwsClaims = jwtTokenizer.getClaims(
                jws,
                jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey())
        );

        Map<String, Object> claims = jwsClaims.getBody();

        String email = (String)claims.get("username");
        String logoutKey = "logout:" + jws;
        valueOperations.set(logoutKey, email, Duration.ofMinutes(jwtTokenizer.getAccessTokenExpirationMinutes()));
        if(memberRepository.findByEmail(pemail).isPresent())
            tokenRepository.delete(tokenRepository.findByMember(memberRepository.findByEmail(pemail).get()).get());
    }
}
