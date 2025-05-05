package ru.yandex.practicum.sht.commerce.order.exception;

public class NoOrderFoundException extends RuntimeException {
    public NoOrderFoundException(String message){
        super(message);
    }
}
