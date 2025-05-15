package ru.yandex.practicum.sht.commerce.order.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.sht.commerce.ia.controller.OrderOperations;
import ru.yandex.practicum.sht.commerce.ia.dto.order.CreateNewOrderRequest;
import ru.yandex.practicum.sht.commerce.ia.dto.order.OrderDto;
import ru.yandex.practicum.sht.commerce.ia.dto.order.ProductReturnRequest;
import ru.yandex.practicum.sht.commerce.order.service.OrderService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/order")
public class OrderController implements OrderOperations {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public List<OrderDto> getOrder(@RequestParam @NotNull String username, Pageable pageable) {
        return orderService.getOrder(username, pageable);
    }

    @PutMapping
    public OrderDto createOrder(@RequestBody @Valid CreateNewOrderRequest request) {
        return orderService.createOrder(request);
    }

    @PostMapping("/return")
    public OrderDto returnOrder(@RequestBody @Valid ProductReturnRequest request) {
        return orderService.returnOrder(request);
    }

    @PostMapping("/payment")
    public OrderDto payOrder(@RequestBody UUID orderId) {
        return orderService.payOrder(orderId);
    }

    @PostMapping("/payment/failed")
    public OrderDto payOrderFailed(@RequestBody UUID orderId) {
        return orderService.payOrderFailed(orderId);
    }

    @PostMapping("/delivery")
    public OrderDto deliverOrder(@RequestBody UUID orderId) {
        return orderService.deliverOrder(orderId);
    }

    @PostMapping("/delivery/failed")
    public OrderDto deliverOrderFailed(@RequestBody UUID orderId) {
        return orderService.deliverOrderFailed(orderId);
    }

    @PostMapping("/completed")
    public OrderDto completeOrder(@RequestBody UUID orderId) {
        return orderService.completeOrder(orderId);
    }

    @PostMapping("/calculate/total")
    public OrderDto calculateTotalOrder(@RequestBody UUID orderId) {
        return orderService.calculateTotalOrder(orderId);
    }

    @PostMapping("/calculate/delivery")
    public OrderDto calculateDeliveryOrder(@RequestBody UUID orderId) {
        return orderService.calculateDeliveryOrder(orderId);
    }

    @PostMapping("/assembly")
    public OrderDto assemblyOrder(@RequestBody UUID orderId) {
        return orderService.assemblyOrder(orderId);
    }

    @PostMapping("/assembly/failed")
    public OrderDto assemblyOrderFailed(@RequestBody UUID orderId) {
        return orderService.assemblyOrderFailed(orderId);
    }
}
