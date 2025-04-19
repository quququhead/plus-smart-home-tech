package ru.yandex.practicum.sht.commerce.warehouse.feign;

import org.springframework.cloud.openfeign.FeignClient;
import ru.yandex.practicum.sht.commerce.ia.controller.ShoppingStoreOperations;

@FeignClient(name = "shopping-store", path = "/api/v1/shopping-store")
public interface ShoppingStoreClient extends ShoppingStoreOperations {
}
