package ru.yandex.practicum.sht.commerce.ia.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.sht.commerce.ia.dto.sc.ChangeProductQuantityRequest;
import ru.yandex.practicum.sht.commerce.ia.dto.sc.ShoppingCartDto;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public interface ShoppingCartOperations {
    @GetMapping
    ShoppingCartDto getShoppingCart(@RequestParam @NotNull String username);

    @PutMapping
    ShoppingCartDto addProductsToShoppingCart(@RequestParam @NotNull String username, @RequestBody @NotNull Map<UUID, Long> products);

    @DeleteMapping
    void deactivateShoppingCart(@RequestParam @NotNull String username);

    @PostMapping("/remove")
    ShoppingCartDto removeProductsFromShoppingCart(@RequestParam @NotNull String username, @RequestBody @NotNull Set<UUID> products);

    @PostMapping("/change-quantity")
    ShoppingCartDto changeProductQuantity(@RequestParam @NotNull String username, @RequestBody @Valid ChangeProductQuantityRequest productQuantity);
}
