package ru.yandex.practicum.sht.commerce.payment.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.sht.commerce.ia.dto.order.OrderDto;
import ru.yandex.practicum.sht.commerce.ia.dto.payment.PaymentDto;
import ru.yandex.practicum.sht.commerce.ia.dto.payment.PaymentState;
import ru.yandex.practicum.sht.commerce.ia.dto.ss.ProductDto;
import ru.yandex.practicum.sht.commerce.payment.exception.NoOrderFoundException;
import ru.yandex.practicum.sht.commerce.payment.exception.NotEnoughInfoInOrderToCalculateException;
import ru.yandex.practicum.sht.commerce.payment.feign.OrderClient;
import ru.yandex.practicum.sht.commerce.payment.feign.ShoppingStoreClient;
import ru.yandex.practicum.sht.commerce.payment.mapper.PaymentMapper;
import ru.yandex.practicum.sht.commerce.payment.model.Payment;
import ru.yandex.practicum.sht.commerce.payment.repository.PaymentRepository;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private static final BigDecimal TAX_COEFF = BigDecimal.valueOf(0.1);

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final ShoppingStoreClient shoppingStoreClient;
    private final OrderClient orderClient;

    public PaymentServiceImpl(OrderClient orderClient, PaymentRepository paymentRepository, PaymentMapper paymentMapper, ShoppingStoreClient shoppingStoreClient) {
        this.orderClient = orderClient;
        this.paymentRepository = paymentRepository;
        this.paymentMapper = paymentMapper;
        this.shoppingStoreClient = shoppingStoreClient;
    }

    @Override
    public PaymentDto createPayment(OrderDto order) {
        BigDecimal totalPrice = order.getTotalPrice();
        BigDecimal deliveryPrice = order.getDeliveryPrice();
        BigDecimal productPrice = order.getProductPrice();
        if (totalPrice == null || deliveryPrice == null || productPrice == null) {
            throw new NotEnoughInfoInOrderToCalculateException(String.format("Not enough info about totalPrice or deliveryPrice or productPrice for order %s to calculate", order.getOrderId()));
        }
        BigDecimal feeTotal = productPrice.multiply(TAX_COEFF);
        Payment payment = Payment.builder()
                .orderId(order.getOrderId())
                .totalPayment(totalPrice)
                .deliveryTotal(deliveryPrice)
                .feeTotal(feeTotal)
                .paymentState(PaymentState.PENDING)
                .build();
        return paymentMapper.mapToPaymentDto(paymentRepository.save(payment));
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal calculateTotalCoast(OrderDto order) {
        BigDecimal deliveryPrice = order.getDeliveryPrice();
        BigDecimal productPrice = order.getProductPrice();
        if (deliveryPrice == null || productPrice == null) {
            throw new NotEnoughInfoInOrderToCalculateException(String.format("Not enough info about deliveryPrice or productPrice for order %s to calculate", order.getOrderId()));
        }
        BigDecimal taxPrice = productPrice.multiply(TAX_COEFF);
        return productPrice.add(deliveryPrice).add(taxPrice);
    }

    @Override
    public void refundPayment(UUID paymentId) {
        Payment payment = getPaymentById(paymentId);
        payment.setPaymentState(PaymentState.SUCCESS);
        paymentRepository.save(payment);
        orderClient.payOrder(payment.getOrderId());
    }

    private Payment getPaymentById(UUID paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new NoOrderFoundException(String.format("There is no payment with paymentId %s", paymentId)));
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal calculateProductCoast(OrderDto order) {
        BigDecimal productCost = BigDecimal.ZERO;
        for (Map.Entry<UUID, Long> product : order.getProducts().entrySet()) {
            ProductDto productDto = shoppingStoreClient.getProduct(product.getKey());
            productCost = productCost.add(BigDecimal.valueOf(product.getValue()).multiply(productDto.getPrice()));
        }
        return productCost;
    }

    @Override
    public void failPayment(UUID paymentId) {
        Payment payment = getPaymentById(paymentId);
        payment.setPaymentState(PaymentState.FAILED);
        paymentRepository.save(payment);
        orderClient.payOrderFailed(payment.getOrderId());
    }
}
