package ru.yandex.practicum.sht.commerce.warehouse.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.sht.commerce.ia.controller.WarehouseOperations;
import ru.yandex.practicum.sht.commerce.ia.dto.sc.ShoppingCartDto;
import ru.yandex.practicum.sht.commerce.ia.dto.warehouse.*;
import ru.yandex.practicum.sht.commerce.warehouse.service.WarehouseService;

import java.util.Map;
import java.util.UUID;

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

    @PostMapping("/shipped")
    public void shippedToDelivery(@RequestBody @Valid ShippedToDeliveryRequest request) {
        warehouseService.shippedToDelivery(request);
    }

    @PostMapping("/return")
    public void acceptReturn(@RequestBody Map<UUID, Long> products) {
        warehouseService.acceptReturn(products);
    }

    @PostMapping("/check")
    public BookedProductsDto checkProductQuantity(@RequestBody @Valid ShoppingCartDto shoppingCartDto) {
        return warehouseService.checkProductQuantity(shoppingCartDto);
    }

    @PostMapping("/assembly")
    public BookedProductsDto assemblyProducts(@RequestBody @Valid AssemblyProductsForOrderRequest request) {
        return warehouseService.assemblyProducts(request);
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
