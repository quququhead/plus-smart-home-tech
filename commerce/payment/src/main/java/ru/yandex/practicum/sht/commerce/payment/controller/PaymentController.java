package ru.yandex.practicum.sht.commerce.payment.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.sht.commerce.ia.controller.PaymentOperations;
import ru.yandex.practicum.sht.commerce.ia.dto.order.OrderDto;
import ru.yandex.practicum.sht.commerce.ia.dto.payment.PaymentDto;
import ru.yandex.practicum.sht.commerce.payment.service.PaymentService;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payment")
public class PaymentController implements PaymentOperations {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public PaymentDto createPayment(@RequestParam @Valid OrderDto order) {
        return paymentService.createPayment(order);
    }

    @PostMapping("/totalCost")
    public BigDecimal calculateTotalCoast(@RequestParam @Valid OrderDto order) {
        return paymentService.calculateTotalCoast(order);
    }

    @PostMapping("/refund")
    public void refundPayment(@RequestBody UUID paymentId) {
        paymentService.refundPayment(paymentId);
    }

    @PostMapping("/productCost")
    public BigDecimal calculateProductCoast(@RequestParam @Valid OrderDto order) {
        return paymentService.calculateProductCoast(order);
    }

    @PostMapping("/failed")
    public void failPayment(@RequestBody UUID paymentId) {
        paymentService.failPayment(paymentId);
    }
}
