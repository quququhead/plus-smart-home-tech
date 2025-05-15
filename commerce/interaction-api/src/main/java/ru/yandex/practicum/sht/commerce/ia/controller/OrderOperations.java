package ru.yandex.practicum.sht.commerce.ia.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.sht.commerce.ia.dto.order.CreateNewOrderRequest;
import ru.yandex.practicum.sht.commerce.ia.dto.order.OrderDto;
import ru.yandex.practicum.sht.commerce.ia.dto.order.ProductReturnRequest;

import java.util.List;
import java.util.UUID;

public interface OrderOperations {
    @GetMapping
    List<OrderDto> getOrder(@RequestParam @NotNull String username, Pageable pageable);

    @PutMapping
    OrderDto createOrder(@RequestBody @Valid CreateNewOrderRequest request);

    @PostMapping("/return")
    OrderDto returnOrder(@RequestBody @Valid ProductReturnRequest request);

    @PostMapping("/payment")
    OrderDto payOrder(@RequestBody UUID orderId);

    @PostMapping("/payment/failed")
    OrderDto payOrderFailed(@RequestBody UUID orderId);

    @PostMapping("/delivery")
    OrderDto deliverOrder(@RequestBody UUID orderId);

    @PostMapping("/delivery/failed")
    OrderDto deliverOrderFailed(@RequestBody UUID orderId);

    @PostMapping("/completed")
    OrderDto completeOrder(@RequestBody UUID orderId);

    @PostMapping("/calculate/total")
    OrderDto calculateTotalOrder(@RequestBody UUID orderId);

    @PostMapping("/calculate/delivery")
    OrderDto calculateDeliveryOrder(@RequestBody UUID orderId);

    @PostMapping("/assembly")
    OrderDto assemblyOrder(@RequestBody UUID orderId);

    @PostMapping("/assembly/failed")
    OrderDto assemblyOrderFailed(@RequestBody UUID orderId);
}
