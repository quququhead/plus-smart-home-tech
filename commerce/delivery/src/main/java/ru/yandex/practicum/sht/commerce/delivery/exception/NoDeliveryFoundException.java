package ru.yandex.practicum.sht.commerce.delivery.exception;

public class NoDeliveryFoundException extends RuntimeException {
    public NoDeliveryFoundException(String message){
        super(message);
    }
}
