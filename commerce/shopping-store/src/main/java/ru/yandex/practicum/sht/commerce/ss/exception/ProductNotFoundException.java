package ru.yandex.practicum.sht.commerce.ss.exception;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(String message){
        super(message);
    }
}
