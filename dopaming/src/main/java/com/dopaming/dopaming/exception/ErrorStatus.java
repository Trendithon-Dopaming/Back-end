
package com.dopaming.dopaming.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST,"잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN,  "금지된 요청입니다."),
    _ENUM_TYPE_NOT_MATCH(HttpStatus.BAD_REQUEST,  "일치하는 타입이 없습니다"),

    USER_NOT_FOUND(HttpStatus.BAD_REQUEST,"존재하지 않는 사용자입니다.");




    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public ErrorReasonDTO getReason() {
        return ErrorReasonDTO.builder()
                .message(message)
                .httpStatus(httpStatus)
                .build();
    }

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .message(message)
                .httpStatus(httpStatus)
                .build();
    }
}
