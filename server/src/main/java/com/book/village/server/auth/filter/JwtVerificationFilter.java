package com.book.village.server.auth.filter;

import com.book.village.server.auth.jwt.tokenizer.JwtTokenizer;
import com.book.village.server.auth.utils.CustomAuthorityUtils;
import com.book.village.server.domain.member.entity.Member;
import com.book.village.server.domain.member.service.MemberService;
import com.book.village.server.global.exception.CustomLogicException;
import com.book.village.server.global.exception.ExceptionCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class JwtVerificationFilter extends OncePerRequestFilter {
    private final JwtTokenizer jwtTokenizer;
    private final CustomAuthorityUtils authorityUtils;
    private final RedisTemplate redisTemplate;
    private final MemberService memberService;

    public JwtVerificationFilter(JwtTokenizer jwtTokenizer, CustomAuthorityUtils authorityUtils, RedisTemplate redisTemplate,MemberService memberService) {
        this.jwtTokenizer = jwtTokenizer;
        this.authorityUtils = authorityUtils;
        this.redisTemplate = redisTemplate;
        this.memberService = memberService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            Map<String, Object> claims = verifyJws(request);
            verifyLogoutToken(request);
            verifyActiveMember(claims);
            setAuthenticationToContext(claims);
        } catch (SignatureException se) {
            throw new CustomLogicException(ExceptionCode.TOKEN_INVALID);
        } catch (ExpiredJwtException ee) {
            throw new CustomLogicException(ExceptionCode.TOKEN_INVALID);
        } catch (Exception e) {
            throw new CustomLogicException(ExceptionCode.TOKEN_INVALID);
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String authorization = request.getHeader("Authorization");
        return authorization == null || !authorization.startsWith("Bearer");
    }

    private Map<String, Object> verifyJws(HttpServletRequest request) {
        String jws = request.getHeader("Authorization").replace("Bearer ", "");
        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());
        Jws<Claims> claimsJws= jwtTokenizer.getClaims(jws, base64EncodedSecretKey);
        Map<String, Object> claims = claimsJws.getBody();

        return claims;
    }
    private void verifyLogoutToken(HttpServletRequest request) throws Exception {
        String jws = request.getHeader("Authorization").replace("Bearer ", "");
        ValueOperations valueOperations = redisTemplate.opsForValue();
        
        String email = (String)valueOperations.get("logout:" + jws);

        if (email != null) {
            throw new Exception();
        }
    }
    private void verifyActiveMember(Map<String, Object> claims){
        String username = (String) claims.get("username");
        if(memberService.findMember(username).getMemberStatus()== Member.MemberStatus.MEMBER_QUIT){
            throw new CustomLogicException(ExceptionCode.MEMBER_STATUS_QUIT);
        }
    }

    private void setAuthenticationToContext(Map<String, Object> claims) {
        String username = (String) claims.get("username");
        List<GrantedAuthority> authorities = authorityUtils.createAuthorities((List)claims.get("roles"));
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
