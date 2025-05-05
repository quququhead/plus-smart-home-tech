package ru.yandex.practicum.sht.commerce.delivery.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.sht.commerce.delivery.service.DeliveryService;
import ru.yandex.practicum.sht.commerce.ia.controller.DeliveryOperations;
import ru.yandex.practicum.sht.commerce.ia.dto.delivery.DeliveryDto;
import ru.yandex.practicum.sht.commerce.ia.dto.order.OrderDto;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/delivery")
public class DeliveryController implements DeliveryOperations {

    private final DeliveryService deliveryService;

    public DeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @PutMapping
    public DeliveryDto createDelivery(@RequestBody @Valid DeliveryDto request) {
        return deliveryService.createDelivery(request);
    }

    @PostMapping("/successful")
    public void successDelivery(@RequestBody UUID orderId) {
        deliveryService.successDelivery(orderId);
    }

    @PostMapping("/picked")
    public void pickDelivery(@RequestBody UUID orderId) {
        deliveryService.pickDelivery(orderId);
    }

    @PostMapping("/failed")
    public void failDelivery(@RequestBody UUID orderId) {
        deliveryService.failDelivery(orderId);
    }

    @PostMapping("/cost")
    public BigDecimal costDelivery(@RequestBody @Valid OrderDto request) {
        return deliveryService.costDelivery(request);
    }
}
