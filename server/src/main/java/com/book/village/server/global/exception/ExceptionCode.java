package com.book.village.server.global.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
public enum ExceptionCode {

    TITLE_NONE(400, "TITLE_NONE"),
    MEMBER_NOT_FOUND(404, "member not found"),

    MEMBER_DUPLICATE(409, "MEMBER_DUPLICATE"),
    ALREADY_LOGOUT_MEMBER(409, "already logout member"),
    REFRESH_TOKEN_NOT_FOUND(404, "refresh token not found"),
    EXPIRED_REFRESH_TOKEN(401, "refresh token expired"),

    TOKEN_INVALID(401 , "TOKEN_INVALID"),

    REQUEST_NOT_FOUND(404,"Request not found"),
    REQUEST_WRITER_NOT_MATCH(409, "Request writer not match");


    @Getter
    private final int code;

    @Getter
    private final String message;

    ExceptionCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
