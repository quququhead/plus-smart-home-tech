package ru.yandex.practicum.sht.commerce.sc.exception;

public class NotAuthorizedUserException extends RuntimeException {
    public NotAuthorizedUserException(String message) {
        super(message);
    }
}
