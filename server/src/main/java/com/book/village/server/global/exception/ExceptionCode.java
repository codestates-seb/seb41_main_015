package com.book.village.server.global.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
public enum ExceptionCode {

    TITLE_NONE(400, "TITLE_NONE"),
    MEMBER_NOT_FOUND(404, "member not found"),
    MEMBER_STATUS_QUIT(404, "member status is quit"),

    MEMBER_DUPLICATE(409, "MEMBER_DUPLICATE"),
    ALREADY_LOGOUT_MEMBER(409, "already logout member"),
    REFRESH_TOKEN_NOT_FOUND(404, "refresh token not found"),
    EXPIRED_REFRESH_TOKEN(401, "refresh token expired"),

    TOKEN_INVALID(401 , "TOKEN_INVALID");

    @Getter
    private final int code;

    @Getter
    private final String message;

    ExceptionCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
