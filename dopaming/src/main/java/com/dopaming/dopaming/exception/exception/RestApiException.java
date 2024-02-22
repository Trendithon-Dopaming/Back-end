package com.dopaming.dopaming.exception.exception;

import com.dopaming.dopaming.exception.errorCode.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RestApiException extends RuntimeException{

    private final ErrorCode errorCode;

}
