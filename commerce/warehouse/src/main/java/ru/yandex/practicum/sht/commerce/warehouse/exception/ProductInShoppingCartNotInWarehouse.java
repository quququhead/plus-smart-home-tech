package ru.yandex.practicum.sht.commerce.warehouse.exception;

public class ProductInShoppingCartNotInWarehouse extends RuntimeException{
    public ProductInShoppingCartNotInWarehouse(String message) {
        super(message);
    }
}
