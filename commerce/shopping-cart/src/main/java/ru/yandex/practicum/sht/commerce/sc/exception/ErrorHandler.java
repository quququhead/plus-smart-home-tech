package ru.yandex.practicum.sht.commerce.sc.exception;

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
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleUnauthorizedException(NotAuthorizedUserException exception) {
        return getErrorResponse(exception, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(NoProductsInShoppingCartException exception) {
        return getErrorResponse(exception, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequestException(DeactivatedShoppingCartException exception) {
        return getErrorResponse(exception, HttpStatus.BAD_REQUEST);
    }
}
