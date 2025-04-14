package ru.yandex.practicum.sht.commerce.sc.exception;

public class NoProductsInShoppingCartException extends RuntimeException {
    public NoProductsInShoppingCartException(String message){
        super(message);
    }
}
