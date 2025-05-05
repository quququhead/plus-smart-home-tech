package ru.yandex.practicum.sht.commerce.order.service;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.sht.commerce.ia.dto.order.CreateNewOrderRequest;
import ru.yandex.practicum.sht.commerce.ia.dto.order.OrderDto;
import ru.yandex.practicum.sht.commerce.ia.dto.order.ProductReturnRequest;
import ru.yandex.practicum.sht.commerce.ia.dto.order.dict.OrderState;
import ru.yandex.practicum.sht.commerce.ia.dto.warehouse.AssemblyProductsForOrderRequest;
import ru.yandex.practicum.sht.commerce.order.exception.NoOrderFoundException;
import ru.yandex.practicum.sht.commerce.order.exception.NotAuthorizedUserException;
import ru.yandex.practicum.sht.commerce.order.feign.DeliveryClient;
import ru.yandex.practicum.sht.commerce.order.feign.PaymentClient;
import ru.yandex.practicum.sht.commerce.order.feign.WarehouseClient;
import ru.yandex.practicum.sht.commerce.order.mapper.OrderMapper;
import ru.yandex.practicum.sht.commerce.order.model.Order;
import ru.yandex.practicum.sht.commerce.order.repository.OrderRepository;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final WarehouseClient warehouseClient;
    private final PaymentClient paymentClient;
    private final DeliveryClient deliveryClient;

    public OrderServiceImpl(DeliveryClient deliveryClient, OrderRepository orderRepository, OrderMapper orderMapper, WarehouseClient warehouseClient, PaymentClient paymentClient) {
        this.deliveryClient = deliveryClient;
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.warehouseClient = warehouseClient;
        this.paymentClient = paymentClient;
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDto> getOrder(String username, Pageable pageable) {
        checkUserIsAuthorized(username);
        return orderRepository.findAllByUsername(username, pageable).stream().map(orderMapper::mapToOrderDto).toList();
    }

    private void checkUserIsAuthorized(String username) {
        if (username.isBlank()) {
            throw new NotAuthorizedUserException("Username is blank");
        }
    }

    @Override
    public OrderDto createOrder(CreateNewOrderRequest request) {
        return orderMapper.mapToOrderDto(orderRepository.save(Order.builder()
                .shoppingCartId(request.getShoppingCart().getShoppingCartId())
                .products(request.getShoppingCart().getProducts())
                .state(OrderState.NEW)
                .build()));
    }

    @Override
    public OrderDto returnOrder(ProductReturnRequest request) {
        Order order = getOrderById(request.getOrderId());
        warehouseClient.acceptReturn(request.getProducts());
        order.setState(OrderState.PRODUCT_RETURNED);
        return orderMapper.mapToOrderDto(orderRepository.save(order));
    }

    private Order getOrderById(UUID orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new NoOrderFoundException(String.format("There is no order with orderId %s", orderId)));
    }

    @Override
    public OrderDto payOrder(UUID orderId) {
        Order order = getOrderById(orderId);
        paymentClient.refundPayment(order.getPaymentId());
        order.setState(OrderState.PAID);
        return orderMapper.mapToOrderDto(orderRepository.save(order));
    }

    @Override
    public OrderDto payOrderFailed(UUID orderId) {
        Order order = getOrderById(orderId);
        paymentClient.failPayment(order.getPaymentId());
        order.setState(OrderState.PAYMENT_FAILED);
        return orderMapper.mapToOrderDto(orderRepository.save(order));
    }

    @Override
    public OrderDto deliverOrder(UUID orderId) {
        Order order = getOrderById(orderId);
        deliveryClient.successDelivery(order.getDeliveryId());
        order.setState(OrderState.DELIVERED);
        return orderMapper.mapToOrderDto(orderRepository.save(order));
    }

    @Override
    public OrderDto deliverOrderFailed(UUID orderId) {
        Order order = getOrderById(orderId);
        deliveryClient.failDelivery(order.getDeliveryId());
        order.setState(OrderState.DELIVERY_FAILED);
        return orderMapper.mapToOrderDto(orderRepository.save(order));
    }

    @Override
    public OrderDto completeOrder(UUID orderId) {
        Order order = getOrderById(orderId);
        order.setState(OrderState.COMPLETED);
        return orderMapper.mapToOrderDto(orderRepository.save(order));
    }

    @Override
    public OrderDto calculateTotalOrder(UUID orderId) {
        Order order = getOrderById(orderId);
        order.setTotalPrice(paymentClient.calculateTotalCoast(orderMapper.mapToOrderDto(order)));
        return orderMapper.mapToOrderDto(orderRepository.save(order));
    }

    @Override
    public OrderDto calculateDeliveryOrder(UUID orderId) {
        Order order = getOrderById(orderId);
        order.setDeliveryPrice(deliveryClient.costDelivery(orderMapper.mapToOrderDto(order)));
        return orderMapper.mapToOrderDto(orderRepository.save(order));
    }

    @Override
    public OrderDto assemblyOrder(UUID orderId) {
        Order order = getOrderById(orderId);
        warehouseClient.assemblyProducts(AssemblyProductsForOrderRequest.builder()
                .products(order.getProducts())
                .orderId(order.getOrderId())
                .build());
        order.setState(OrderState.ASSEMBLED);
        return orderMapper.mapToOrderDto(orderRepository.save(order));
    }

    @Override
    public OrderDto assemblyOrderFailed(UUID orderId) {
        Order order = getOrderById(orderId);
        order.setState(OrderState.ASSEMBLY_FAILED);
        return orderMapper.mapToOrderDto(orderRepository.save(order));
    }
}
