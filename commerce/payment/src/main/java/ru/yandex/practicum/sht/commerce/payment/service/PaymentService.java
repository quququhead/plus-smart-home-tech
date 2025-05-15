package ru.yandex.practicum.sht.commerce.payment.service;

import ru.yandex.practicum.sht.commerce.ia.dto.order.OrderDto;
import ru.yandex.practicum.sht.commerce.ia.dto.payment.PaymentDto;

import java.math.BigDecimal;
import java.util.UUID;

public interface PaymentService {
    PaymentDto createPayment(OrderDto order);

    BigDecimal calculateTotalCoast(OrderDto order);

    void refundPayment(UUID paymentId);

    BigDecimal calculateProductCoast(OrderDto order);

    void failPayment(UUID paymentId);
}
