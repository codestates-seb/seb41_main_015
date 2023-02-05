package com.book.village.server.auth.handler;

import com.book.village.server.auth.jwt.tokenizer.JwtTokenizer;
import com.book.village.server.auth.jwt.entity.RefreshToken;
import com.book.village.server.auth.jwt.repository.RefreshTokenRepository;
import com.book.village.server.auth.utils.CustomAuthorityUtils;
import com.book.village.server.domain.member.entity.Member;
import com.book.village.server.domain.member.repository.MemberRepository;
import com.book.village.server.domain.member.service.MemberService;
import com.book.village.server.global.exception.CustomLogicException;
import com.book.village.server.global.exception.ExceptionCode;
import com.book.village.server.global.utils.RedirectType;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OAuth2MemberSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtTokenizer jwtTokenizer;
    private final CustomAuthorityUtils authorityUtils;
    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final RedirectType redirectType;

    public OAuth2MemberSuccessHandler(JwtTokenizer jwtTokenizer, CustomAuthorityUtils authorityUtils, MemberService memberService, MemberRepository memberRepository, RefreshTokenRepository refreshTokenRepository, RedirectType redirectType) {
        this.jwtTokenizer = jwtTokenizer;
        this.authorityUtils = authorityUtils;
        this.memberService = memberService;
        this.memberRepository = memberRepository;
        this.refreshTokenRepository=refreshTokenRepository;
        this.redirectType = redirectType;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        var oAuth2User = (OAuth2User)authentication.getPrincipal();
        String email = String.valueOf(oAuth2User.getAttributes().get("email"));
        List<String> authorities = authorityUtils.createRoles(email);
        if(!memberRepository.findByEmail(email).isPresent()){
            Member member = new Member(email);
            memberService.createMember(member);
            redirect(request,response,email, authorities, true);
            return;
        }
        verifyActiveMember(email);
        redirect(request,response,email, authorities, false);
    }
    private void verifyActiveMember(String email){
        if(memberService.findMember(email).getMemberStatus()== Member.MemberStatus.MEMBER_QUIT){
            throw new CustomLogicException(ExceptionCode.MEMBER_STATUS_QUIT);
        }
    }
    private String delegateAccessToken(String username, List<String> authorities) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", username);
        claims.put("roles", authorities);

        String subject = username;
        Date expiration = jwtTokenizer.getTokenExpiration(jwtTokenizer.getAccessTokenExpirationMinutes());

        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());

        String accessToken = jwtTokenizer.generateAccessToken(claims, subject, expiration, base64EncodedSecretKey);

        return accessToken;
    }

    private String delegateRefreshToken(String username) {
        String subject = username;
        Date expiration = jwtTokenizer.getTokenExpiration(jwtTokenizer.getRefreshTokenExpirationMinutes());
        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());

        String refreshToken = jwtTokenizer.generateRefreshToken(subject, expiration, base64EncodedSecretKey);
        RefreshToken token = RefreshToken.builder()
                .refreshToken(refreshToken)
                .member(memberService.findMember(username))
                .build();
        if(refreshTokenRepository.findByMember(memberService.findMember(username)).isPresent()){
            refreshTokenRepository.delete(refreshTokenRepository.findByMember(memberService.findMember(username)).get());
            refreshTokenRepository.save(token);
            return refreshToken;
        }

        refreshTokenRepository.save(token);
        return refreshToken;
    }

    private void redirect(HttpServletRequest request, HttpServletResponse response, String username, List<String> authorities, boolean newbie) throws IOException {
        String accessToken = delegateAccessToken(username, authorities);
        String refreshToken = delegateRefreshToken(username);

        String uri = createURI(accessToken, refreshToken, newbie).toString();
        getRedirectStrategy().sendRedirect(request, response, uri);
    }

    private URI createURI(String accessToken, String refreshToken, boolean newbie) {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("access_token", accessToken);
        queryParams.add("refresh_token", refreshToken);
        if (newbie) queryParams.add("membership", "new");
        else queryParams.add("membership", "existing");
        if (redirectType.getServerType().equals("local")) {
            return UriComponentsBuilder
                    .newInstance()
                    .scheme("http")
                    .host("localhost")
                    .path("/receive-token.html")
                    .queryParams(queryParams)
                    .build()
                    .toUri();
        }
        return UriComponentsBuilder
                .newInstance()
                .scheme("http")
                .host("bookvillage.kro.kr")
                .path("/oauth")
                .queryParams(queryParams)
                .build()
                .toUri();
    }
}
