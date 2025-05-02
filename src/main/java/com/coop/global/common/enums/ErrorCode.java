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
    ACCESS_DENIED(HttpStatus.UNAUTHORIZED, "401", "로그인이 필요합니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "403", "접근 권한이 없습니다."),
    TOO_MANY_REQUEST(HttpStatus.TOO_MANY_REQUESTS, "429", "현재 요청이 많아 처리할 수 없습니다. 잠시 후 다시 시도해주세요."),

    // Security
    TOKEN_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "ST401", "유효한 JWT 토큰이 존재하지 않습니다."),
    WEBSOCKET_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "SW401", "WEBSOCKET 연결에 실패하였습니다."),

    // Member
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "M404", "존재하지 않는 유저입니다."),
    ALREADY_EXIST_EMAIL(HttpStatus.BAD_REQUEST, "M400", "이미 존재하는 이메일입니다."),
    INVALID_EMAIL(HttpStatus.UNAUTHORIZED, "M401", "이메일이 잘못되었습니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "M401", "패스워드가 잘못되었습니다."),

    // Post
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "P404", "존재하지 않는 게시물입니다."),
    INVALID_POST_CATEGORY(HttpStatus.BAD_REQUEST, "P400", "게시물 카테고리가 잘못되었습니다."),

    // Comment
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "C404", "존재하지 않는 댓글입니다."),
    INVALID_PARENT_ID(HttpStatus.BAD_REQUEST, "C404", "답글을 달려는 댓글을 찾을 수 없습니다."),

    // Game
    GAME_NOT_FOUND(HttpStatus.NOT_FOUND, "G404", "존재하지 않는 게임입니다."),

    // Room
    ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "R404", "존재하지 않는 방입니다."),
    PLAYER_NOT_IN_ROOM(HttpStatus.NOT_FOUND, "R404", "유저가 해당 방에 없습니다."),
    PLAYER_ALREADY_IN_ROOM(HttpStatus.CONFLICT, "R409", "이미 방에 접속중입니다."),
    ROOM_IS_FULL(HttpStatus.BAD_REQUEST, "R400", "방이 가득 찼습니다."),
    REDIS_ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "R404", "존재하지 않는 방입니다."),
    REDIS_ROOM_NO_PLAYERS(HttpStatus.NOT_FOUND, "R404", "방에 인원이 존재하지 않습니다."),
    EXCEEDS_CURRENT_PLAYER_COUNT(HttpStatus.BAD_REQUEST, "R400", "현재 인원이 변경하려는 최대 인원보다 많습니다"),

    // Redisson Lock
    LOCK_ACQUISITION_FAILED(HttpStatus.TOO_MANY_REQUESTS, "L429", "락 획득에 실패했습니다. 잠시 후 다시 시도해주세요."),

    //History
    HISTORY_NOT_FOUND(HttpStatus.NOT_FOUND, "H404", "존재하지 않는 방입니다."),

    //Rating
    RATING_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "Rt400", "이미 존재하는 평점입니다."),

    //Notification
    INVALID_NOTIFICATION_TYPE(HttpStatus.BAD_REQUEST, "N400", "존재하지 않는 알림 타입입니다."),
    NOTIFICATION_NOT_FOUND(HttpStatus.NOT_FOUND, "M404", "존재하지 않는 알림입니다."),
    ;
    private final HttpStatus status;
    private final String code;
    private final String message;
}

