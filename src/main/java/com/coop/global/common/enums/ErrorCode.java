package com.coop.global.common.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;
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
    FORBIDDEN(HttpStatus.FORBIDDEN, "403", "접근 권한이 없습니다."),
    TOO_MANY_REQUEST(HttpStatus.TOO_MANY_REQUESTS, "429", "현재 요청이 많아 처리할 수 없습니다. 잠시 후 다시 시도해주세요."),

    // Member

    // Post

    // Game
    GAME_NOT_FOUND(HttpStatus.NOT_FOUND, "G404", "존재하지 않는 게임입니다."),

    // Room
    ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "R404", "존재하지 않는 방입니다.")

    ;
    private final HttpStatus status;
    private final String code;
    private final String message;
}

