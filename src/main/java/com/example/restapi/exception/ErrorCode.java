package com.example.restapi.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    //공통 에러
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "INVALID INPUT", "잘못된 입력값입니다."),
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR", "서버 오류가 발생했습니다."),

    // Todo 에러
    TODO_NOT_FOUND(HttpStatus.NOT_FOUND, "TODO_NOT_FOUND", "할일을 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
