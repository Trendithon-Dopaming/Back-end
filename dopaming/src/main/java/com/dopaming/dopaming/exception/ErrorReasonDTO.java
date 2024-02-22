package com.dopaming.dopaming.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Builder
@Getter
@AllArgsConstructor
@JsonPropertyOrder({"httpStatus", "message"})
public class ErrorReasonDTO {
    private final HttpStatus httpStatus;
    private final String message;
}
