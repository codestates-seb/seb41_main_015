package com.book.village.server.global.utils;

import org.springframework.http.HttpHeaders;

public class GenerateMockToken {
    private static final String AUTHORIZATION = "Authorization";
    public static String createMockToken() {
        return "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiZXhwIjoxNjcxNjg5Nzg4fQ.eFeEyh5F5ilhUfK28DzIxNPscqrlo5d9kNcOZYgbsUs";
    }
    public static String createMockRefreshToken(){
        return "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJvcmlvbmNzeUBnbWFpbC5jb20iLCJpYXQiOjE2NzM0NDE1MzMsImV4cCI6MTY3MzQ2NjczM30.xxnf6XFYFvQfyCRTr0jFBK5sr7Ol2yDfQcTjQ4K7hiw";
    }
    public static HttpHeaders getMockHeaderToken(){
        HttpHeaders headers = new HttpHeaders();
        headers.add(AUTHORIZATION, createMockToken());
        return headers;
    }
    public static HttpHeaders getMockHeaderRefreshToken(){
        HttpHeaders headers = new HttpHeaders();
        headers.add(AUTHORIZATION, createMockRefreshToken());
        return headers;
    }
}
