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
    ALREADY_EXIST_EMAIL(HttpStatus.BAD_REQUEST, "400", "이미 존재하는 이메일입니다."),
    TOKEN_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "401", "존재하지 않는 토큰입니다."),
    INVALID_EMAIL(HttpStatus.UNAUTHORIZED, "401", "이메일이 잘못되었습니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "401", "패스워드가 잘못되었습니다."),

    // Post

    // Game
    GAME_NOT_FOUND(HttpStatus.NOT_FOUND, "G404", "존재하지 않는 게임입니다."),

    // Room
    ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "R404", "존재하지 않는 방입니다."),
    MEMBER_NOT_IN_ROOM(HttpStatus.NOT_FOUND, "R400", "유저가 해당 방에 없습니다."),
    MEMBER_ALREADY_IN_ROOM(HttpStatus.CONFLICT, "R409", "이미 방에 접속중입니다."),
    ROOM_IS_FULL(HttpStatus.BAD_REQUEST, "R400", "방이 가득 찼습니다.")


    ;
    private final HttpStatus status;
    private final String code;
    private final String message;
}

