package ru.yandex.practicum.sht.commerce.payment.exception;

public class NotEnoughInfoInOrderToCalculateException extends RuntimeException{
    public NotEnoughInfoInOrderToCalculateException(String message) {
        super(message);
    }
}
