package ru.yandex.practicum.sht.commerce.ia.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.sht.commerce.ia.dto.sc.ShoppingCartDto;
import ru.yandex.practicum.sht.commerce.ia.dto.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.sht.commerce.ia.dto.warehouse.AddressDto;
import ru.yandex.practicum.sht.commerce.ia.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.sht.commerce.ia.dto.warehouse.NewProductInWarehouseRequest;

public interface WarehouseOperations {
    @PutMapping
    void addProductToWarehouse(@RequestBody @Valid NewProductInWarehouseRequest warehouseRequest);

    @PostMapping("/check")
    BookedProductsDto checkProductQuantity(ShoppingCartDto shoppingCartDto);

    @PostMapping("/add")
    void acceptProductToWarehouse(AddProductToWarehouseRequest warehouseRequest);

    @GetMapping("/address")
    AddressDto getWarehouseAddress();
}
