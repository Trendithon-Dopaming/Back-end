package com.dopaming.dopaming.exception.errorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode{

    // 추가할 에러
    INACTIVE_USER(HttpStatus.NOT_FOUND, "존재하지 않는 유저입니다.");



    // 기본 에러 형식
    private final HttpStatus httpStatus;
    private final String message;

}
