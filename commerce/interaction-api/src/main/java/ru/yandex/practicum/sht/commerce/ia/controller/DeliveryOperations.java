package ru.yandex.practicum.sht.commerce.ia.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.sht.commerce.ia.dto.delivery.DeliveryDto;
import ru.yandex.practicum.sht.commerce.ia.dto.order.OrderDto;

import java.math.BigDecimal;
import java.util.UUID;

public interface DeliveryOperations {
    @PutMapping
    DeliveryDto createDelivery(@RequestBody @Valid DeliveryDto request);

    @PostMapping("/successful")
    void successDelivery(@RequestBody UUID orderId);

    @PostMapping("/picked")
    void pickDelivery(@RequestBody UUID orderId);

    @PostMapping("/failed")
    void failDelivery(@RequestBody UUID orderId);

    @PostMapping("/cost")
    BigDecimal costDelivery(@RequestBody @Valid OrderDto request);
}
