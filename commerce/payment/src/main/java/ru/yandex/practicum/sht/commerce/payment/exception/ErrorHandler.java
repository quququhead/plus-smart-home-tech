package ru.yandex.practicum.sht.commerce.payment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.sht.commerce.ia.exception.ErrorResponse;

import static ru.yandex.practicum.sht.commerce.ia.exception.ErrorResponseConverter.getErrorResponse;

@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleInternalServerErrorException(RuntimeException exception) {
        return getErrorResponse(exception, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleProductNotFoundException(NoOrderFoundException exception) {
        return getErrorResponse(exception, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleUnauthorizedException(NotEnoughInfoInOrderToCalculateException exception) {
        return getErrorResponse(exception, HttpStatus.BAD_REQUEST);
    }
}
