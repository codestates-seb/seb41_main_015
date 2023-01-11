package com.book.village.server.global.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
public enum ExceptionCode {

    TITLE_NONE(400, "TITLE_NONE"),
    MEMBER_NOT_FOUND(404, "member not found"),
    MEMBER_DUPLICATE(409, "MEMBER_DUPLICATE"),

    BORROW_NOT_FOUND(404, "BORROW_NOT_FOUND"), // 나눔글 존재 하지 않음.
    BORROW_USER_DIFFERENT(403, "BORROW_USER_DIFFERENT"),    // 나눔글 작성자가 아닌 사람이 수정하려고 할 때

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
