package com.book.village.server.global.exception;

import lombok.Getter;

@Getter
public enum ExceptionCode {

    TITLE_NONE(400, "TITLE_NONE"),
    MEMBER_NOT_FOUND(404, "member not found"),

    MEMBER_DUPLICATE(409, "MEMBER_DUPLICATE"),

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
