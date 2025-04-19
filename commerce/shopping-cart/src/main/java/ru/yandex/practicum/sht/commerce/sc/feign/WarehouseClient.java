package ru.yandex.practicum.sht.commerce.sc.feign;

import org.springframework.cloud.openfeign.FeignClient;
import ru.yandex.practicum.sht.commerce.ia.controller.WarehouseOperations;

@FeignClient(name = "warehouse", path = "/api/v1/warehouse")
public interface WarehouseClient extends WarehouseOperations {
}
