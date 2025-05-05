package ru.yandex.practicum.sht.commerce.ia.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.sht.commerce.ia.dto.sc.ShoppingCartDto;
import ru.yandex.practicum.sht.commerce.ia.dto.warehouse.*;

import java.util.Map;
import java.util.UUID;

public interface WarehouseOperations {
    @PutMapping
    void addProductToWarehouse(@RequestBody @Valid NewProductInWarehouseRequest warehouseRequest);

    @PostMapping("/shipped")
    void shippedToDelivery(@RequestBody @Valid ShippedToDeliveryRequest request);

    @PostMapping("/return")
    void acceptReturn(@RequestBody Map<UUID, Long> products);

    @PostMapping("/check")
    BookedProductsDto checkProductQuantity(ShoppingCartDto shoppingCartDto);

    @PostMapping("/assembly")
    BookedProductsDto assemblyProducts(@RequestBody @Valid AssemblyProductsForOrderRequest request);

    @PostMapping("/add")
    void acceptProductToWarehouse(AddProductToWarehouseRequest warehouseRequest);

    @GetMapping("/address")
    AddressDto getWarehouseAddress();
}
