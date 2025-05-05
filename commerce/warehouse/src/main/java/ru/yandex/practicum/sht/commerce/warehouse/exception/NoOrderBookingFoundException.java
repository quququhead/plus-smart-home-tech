package ru.yandex.practicum.sht.commerce.warehouse.exception;

public class NoOrderBookingFoundException extends RuntimeException{
    public NoOrderBookingFoundException(String message) {
        super(message);
    }
}
