package ru.yandex.practicum.sht.commerce.ia.exception;

import org.springframework.http.HttpStatus;

import java.util.Arrays;

public class ErrorResponseConverter {
    public static ErrorResponse getErrorResponse(RuntimeException exception, HttpStatus httpStatus) {
        return ErrorResponse.builder()
                .cause(exception.getCause())
                .stackTrace(Arrays.asList(exception.getStackTrace()))
                .httpStatus(httpStatus)
                .userMessage(exception.getMessage())
                .message(httpStatus.getReasonPhrase())
                .suppressed(Arrays.asList(exception.getSuppressed()))
                .localizedMessage(exception.getLocalizedMessage())
                .build();
    }
}
