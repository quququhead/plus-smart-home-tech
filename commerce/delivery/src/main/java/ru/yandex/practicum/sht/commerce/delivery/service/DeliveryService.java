package ru.yandex.practicum.sht.commerce.delivery.service;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.sht.commerce.ia.dto.delivery.DeliveryDto;
import ru.yandex.practicum.sht.commerce.ia.dto.order.OrderDto;

import java.math.BigDecimal;
import java.util.UUID;

public interface DeliveryService {
    DeliveryDto createDelivery(@RequestBody @Valid DeliveryDto request);

    void successDelivery(@RequestBody UUID orderId);

    void pickDelivery(@RequestBody UUID orderId);

    void failDelivery(@RequestBody UUID orderId);

    BigDecimal costDelivery(@RequestBody @Valid OrderDto request);
}
