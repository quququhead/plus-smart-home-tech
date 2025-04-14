package ru.yandex.practicum.sht.commerce.warehouse.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.sht.commerce.ia.controller.WarehouseOperations;
import ru.yandex.practicum.sht.commerce.ia.dto.sc.ShoppingCartDto;
import ru.yandex.practicum.sht.commerce.ia.dto.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.sht.commerce.ia.dto.warehouse.AddressDto;
import ru.yandex.practicum.sht.commerce.ia.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.sht.commerce.ia.dto.warehouse.NewProductInWarehouseRequest;
import ru.yandex.practicum.sht.commerce.warehouse.service.WarehouseService;

@RestController
@RequestMapping("/api/v1/warehouse")
public class WarehouseController implements WarehouseOperations {

    private final WarehouseService warehouseService;

    public WarehouseController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    @PutMapping
    public void addProductToWarehouse(@RequestBody @Valid NewProductInWarehouseRequest warehouseRequest) {
        warehouseService.addProductToWarehouse(warehouseRequest);
    }

    @PostMapping("/check")
    public BookedProductsDto checkProductQuantity(@RequestBody @Valid ShoppingCartDto shoppingCartDto) {
        return warehouseService.checkProductQuantity(shoppingCartDto);
    }

    @PostMapping("/add")
    public void acceptProductToWarehouse(@RequestBody @Valid AddProductToWarehouseRequest warehouseRequest) {
        warehouseService.acceptProductToWarehouse(warehouseRequest);
    }

    @GetMapping("/address")
    public AddressDto getWarehouseAddress() {
        return warehouseService.getWarehouseAddress();
    }
}
