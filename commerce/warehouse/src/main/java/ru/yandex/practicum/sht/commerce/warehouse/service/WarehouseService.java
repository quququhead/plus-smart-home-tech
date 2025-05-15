package ru.yandex.practicum.sht.commerce.warehouse.service;

import ru.yandex.practicum.sht.commerce.ia.dto.sc.ShoppingCartDto;
import ru.yandex.practicum.sht.commerce.ia.dto.warehouse.*;

import java.util.Map;
import java.util.UUID;

public interface WarehouseService {

    void addProductToWarehouse(NewProductInWarehouseRequest warehouseRequest);

    void shippedToDelivery(ShippedToDeliveryRequest request);

    void acceptReturn(Map<UUID, Long> products);

    BookedProductsDto checkProductQuantity(ShoppingCartDto shoppingCartDto);

    BookedProductsDto assemblyProducts(AssemblyProductsForOrderRequest request);

    void acceptProductToWarehouse(AddProductToWarehouseRequest warehouseRequest);

    AddressDto getWarehouseAddress();
}
