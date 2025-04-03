package com.coop.global.common.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.tomcat.util.http.parser.HttpParser;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    // Common
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "400", "올바르지 않은 요청입니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "405", "잘못된 HTTP 메서드를 호출했습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "500", "서버에 에러가 발생했습니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "404", "존재하지 않는 엔티티입니다."),
    ENTITY_ALREADY_EXISTS(HttpStatus.CONFLICT, "409", "이미 존재하는 엔티티입니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "403", "접근 권한이 없습니다."),
    TOO_MANY_REQUEST(HttpStatus.TOO_MANY_REQUESTS, "429", "현재 요청이 많아 처리할 수 없습니다. 잠시 후 다시 시도해주세요."),

    // Member
    ALREADY_EXIST_EMAIL(HttpStatus.BAD_REQUEST, "400", "이미 존재하는 이메일입니다."),
    TOKEN_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "401", "존재하지 않는 토큰입니다."),
    INVALID_EMAIL(HttpStatus.UNAUTHORIZED, "401", "이메일이 잘못되었습니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED,"401", "패스워드가 잘못되었습니다."),

    // Post
    NOT_FOUND_POST(HttpStatus.NOT_FOUND, "404", "존재하지 않는 게시물입니다."),
    INVALID_POST_CATEGORY(HttpStatus.BAD_REQUEST, "400", "게시물 카테고리가 잘못되었습니다."),

    // Comment
    NOT_FOUND_COMMENT(HttpStatus.NOT_FOUND, "404", "존재하지 않는 댓글입니다."),
    INVALID_PARENT_ID(HttpStatus.BAD_REQUEST, "404", "답글을 달려는 댓글을 찾을 수 없습니다."),
    // Game

    ;
    private final HttpStatus status;
    private final String code;
    private final String message;
}

