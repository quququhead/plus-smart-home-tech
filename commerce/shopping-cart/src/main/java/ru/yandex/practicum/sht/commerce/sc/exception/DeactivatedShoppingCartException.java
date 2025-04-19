package ru.yandex.practicum.sht.commerce.sc.exception;

public class DeactivatedShoppingCartException extends RuntimeException{
    public DeactivatedShoppingCartException(String message) {
        super(message);
    }
}
