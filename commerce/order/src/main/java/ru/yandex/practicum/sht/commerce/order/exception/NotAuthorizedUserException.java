package ru.yandex.practicum.sht.commerce.order.exception;

public class NotAuthorizedUserException extends RuntimeException{
    public NotAuthorizedUserException(String message) {
        super(message);
    }
}
