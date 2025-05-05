package ru.yandex.practicum.sht.commerce.payment.feign;

import org.springframework.cloud.openfeign.FeignClient;
import ru.yandex.practicum.sht.commerce.ia.controller.OrderOperations;

@FeignClient(name = "order", path = "/api/v1/order")
public interface OrderClient extends OrderOperations {
}
