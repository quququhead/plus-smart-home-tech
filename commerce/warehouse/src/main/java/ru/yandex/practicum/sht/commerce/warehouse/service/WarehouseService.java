package ru.yandex.practicum.sht.commerce.warehouse.service;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.sht.commerce.ia.dto.sc.ShoppingCartDto;
import ru.yandex.practicum.sht.commerce.ia.dto.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.sht.commerce.ia.dto.warehouse.AddressDto;
import ru.yandex.practicum.sht.commerce.ia.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.sht.commerce.ia.dto.warehouse.NewProductInWarehouseRequest;

public interface WarehouseService {

    void addProductToWarehouse(@RequestBody @Valid NewProductInWarehouseRequest warehouseRequest);

    BookedProductsDto checkProductQuantity(ShoppingCartDto shoppingCartDto);

    void acceptProductToWarehouse(AddProductToWarehouseRequest warehouseRequest);

    AddressDto getWarehouseAddress();
}
