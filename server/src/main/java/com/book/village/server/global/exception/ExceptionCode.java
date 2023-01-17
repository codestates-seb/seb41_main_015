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

    TOKEN_INVALID(401 , "TOKEN_INVALID"),

    REQUEST_NOT_FOUND(404,"Request not found"),
    REQUEST_WRITER_NOT_MATCH(409, "Request writer not match"),
    REQUEST_COMMENT_NOT_FOUND(404, "request comment not found"),
    REQUEST_COMMENT_USER_DIFFERENT(409, "request comment writer is not matched"),

    BORROW_NOT_FOUND(404, "BORROW_NOT_FOUND"), // 나눔글 존재 하지 않음.
    BORROW_USER_DIFFERENT(403, "BORROW_USER_DIFFERENT"),   // 나눔글 작성자가 아닌 사람이 수정하려고 할 때

    COMMUNITY_NOT_FOUND(404, "community not found"),
    COMMUNITY_USER_DIFFERENT(409, "community writer is not matched"),

    COMMUNITY_COMMENT_NOT_FOUND(404, "community comment not found"),
    COMMUNITY_COMMENT_USER_DIFFERENT(409, "community comment writer is not matched"),

    BOOK_EXISTS(409,"Book exists"),
    BOOK_NOT_FOUND(404, "Book is not found"),

    RATE_USER_DIFFERENT(409, "rate writer is not matched"),
    RATE_NOT_FOUND(404, "rate not found"),
    RATE_DUPLICATE(409, "rate duplicate");

    @Getter
    private final int code;

    @Getter
    private final String message;

    ExceptionCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
