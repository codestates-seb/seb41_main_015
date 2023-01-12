package com.book.village.server.global.utils;

import org.springframework.http.HttpHeaders;

public class GenerateMockToken {
    private static final String AUTHORIZATION = "Authorization";
    public static String createMockToken() {
        return "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiZXhwIjoxNjcxNjg5Nzg4fQ.eFeEyh5F5ilhUfK28DzIxNPscqrlo5d9kNcOZYgbsUs";
    }
    public static HttpHeaders getMockHeaderToken(){
        HttpHeaders headers = new HttpHeaders();
        headers.add(AUTHORIZATION, createMockToken());
        return headers;
    }
}
