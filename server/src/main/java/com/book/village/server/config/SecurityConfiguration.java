package com.book.village.server.config;

import com.book.village.server.auth.filter.AuthExceptionHandlerFilter;
import com.book.village.server.auth.filter.JwtVerificationFilter;
import com.book.village.server.auth.handler.MemberAccessDeniedHandler;
import com.book.village.server.auth.handler.MemberAuthenticationEntryPoint;
import com.book.village.server.auth.handler.OAuth2MemberSuccessHandler;
import com.book.village.server.auth.jwt.tokenizer.JwtTokenizer;
import com.book.village.server.auth.jwt.repository.RefreshTokenRepository;
import com.book.village.server.auth.service.CustomOAuth2MemberService;
import com.book.village.server.auth.utils.CustomAuthorityUtils;
import com.book.village.server.domain.member.repository.MemberRepository;
import com.book.village.server.domain.member.service.MemberService;
import com.book.village.server.global.utils.RedirectType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableRedisRepositories
public class SecurityConfiguration {
    private final JwtTokenizer jwtTokenizer;
    private final CustomAuthorityUtils authorityUtils;
    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final CustomOAuth2MemberService customOAuth2MemberService;
    private final RedisTemplate redisTemplate;
    private final RefreshTokenRepository refreshTokenRepository;
    private final RedirectType redirectType;


    public SecurityConfiguration(JwtTokenizer jwtTokenizer, CustomAuthorityUtils authorityUtils,
                                 MemberService memberService, MemberRepository memberRepository, CustomOAuth2MemberService customOAuth2MemberService, RedisTemplate redisTemplate, RefreshTokenRepository refreshTokenRepository, RedirectType redirectType) {
        this.jwtTokenizer = jwtTokenizer;
        this.authorityUtils = authorityUtils;
        this.memberService = memberService;
        this.memberRepository = memberRepository;
        this.customOAuth2MemberService = customOAuth2MemberService;
        this.redisTemplate = redisTemplate;

        this.refreshTokenRepository = refreshTokenRepository;
        this.redirectType = redirectType;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                .headers().frameOptions().disable()
                .and()
                .csrf().disable()
                .cors(withDefaults())
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin().disable()
                .httpBasic().disable()
                .exceptionHandling()
                .authenticationEntryPoint(new MemberAuthenticationEntryPoint())
                .accessDeniedHandler(new MemberAccessDeniedHandler())
                .and()
                .apply(new CustomFilterConfigurer())
                .and()
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().permitAll())
                .oauth2Login()
                .successHandler(new OAuth2MemberSuccessHandler(jwtTokenizer, authorityUtils, memberService, memberRepository, refreshTokenRepository, redirectType))
                .userInfoEndpoint() // OAuth2 로그인 성공 이후 사용자 정보를 가져올 때 설정을 저장
                .userService(customOAuth2MemberService); // OAuth2 로그인 성공 시, 후작업을 진행할 UserService 인터페이스 구현체 등록

        return http.build();
    }

    public class CustomFilterConfigurer extends AbstractHttpConfigurer<CustomFilterConfigurer, HttpSecurity> {
        @Override
        public void configure(HttpSecurity builder) throws Exception {
            AuthExceptionHandlerFilter authExceptionHandlerFilter = new AuthExceptionHandlerFilter();
            builder.addFilterBefore(authExceptionHandlerFilter, LogoutFilter.class);
            JwtVerificationFilter jwtVerificationFilter = new JwtVerificationFilter(jwtTokenizer, authorityUtils,redisTemplate, memberService);
            builder.addFilterAfter(jwtVerificationFilter, OAuth2LoginAuthenticationFilter.class);
        }
    }
}
