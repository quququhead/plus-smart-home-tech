package ru.yandex.practicum.sht.commerce.sc.service;

import ru.yandex.practicum.sht.commerce.ia.dto.sc.ChangeProductQuantityRequest;
import ru.yandex.practicum.sht.commerce.ia.dto.sc.ShoppingCartDto;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public interface ShoppingCartService {
    ShoppingCartDto getShoppingCart(String username);

    ShoppingCartDto addProductsToShoppingCart(String username, Map<UUID, Long> products);

    void deactivateShoppingCart(String username);

    ShoppingCartDto removeProductsFromShoppingCart(String username, Set<UUID> products);

    ShoppingCartDto changeProductQuantity(String username, ChangeProductQuantityRequest productQuantity);
}
