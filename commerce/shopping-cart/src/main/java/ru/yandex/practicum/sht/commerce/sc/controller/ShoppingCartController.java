package ru.yandex.practicum.sht.commerce.sc.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.sht.commerce.ia.controller.ShoppingCartOperations;
import ru.yandex.practicum.sht.commerce.ia.dto.sc.ChangeProductQuantityRequest;
import ru.yandex.practicum.sht.commerce.ia.dto.sc.ShoppingCartDto;
import ru.yandex.practicum.sht.commerce.sc.service.ShoppingCartService;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/shopping-cart")
public class ShoppingCartController implements ShoppingCartOperations {

    private final ShoppingCartService shoppingCartService;

    public ShoppingCartController(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }

    @GetMapping
    public ShoppingCartDto getShoppingCart(@RequestParam @NotNull String username) {
        return shoppingCartService.getShoppingCart(username);
    }

    @PutMapping
    public ShoppingCartDto addProductsToShoppingCart(@RequestParam @NotNull String username, @RequestBody @NotNull Map<UUID, Long> products) {
        return shoppingCartService.addProductsToShoppingCart(username, products);
    }

    @DeleteMapping
    public void deactivateShoppingCart(@RequestParam @NotNull String username) {
        shoppingCartService.deactivateShoppingCart(username);
    }

    @PostMapping("/remove")
    public ShoppingCartDto removeProductsFromShoppingCart(@RequestParam @NotNull String username, @RequestBody @NotNull Set<UUID> products) {
        return shoppingCartService.removeProductsFromShoppingCart(username, products);
    }

    @PostMapping("/change-quantity")
    public ShoppingCartDto changeProductQuantity(@RequestParam @NotNull String username, @RequestBody @Valid ChangeProductQuantityRequest productQuantity) {
        return shoppingCartService.changeProductQuantity(username, productQuantity);
    }
}
