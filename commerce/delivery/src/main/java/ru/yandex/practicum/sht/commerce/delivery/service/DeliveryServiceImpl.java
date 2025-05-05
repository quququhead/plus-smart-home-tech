package ru.yandex.practicum.sht.commerce.delivery.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.sht.commerce.delivery.exception.NoDeliveryFoundException;
import ru.yandex.practicum.sht.commerce.delivery.feign.OrderClient;
import ru.yandex.practicum.sht.commerce.delivery.feign.WarehouseClient;
import ru.yandex.practicum.sht.commerce.delivery.mapper.DeliveryMapper;
import ru.yandex.practicum.sht.commerce.delivery.model.Address;
import ru.yandex.practicum.sht.commerce.delivery.model.Delivery;
import ru.yandex.practicum.sht.commerce.delivery.repository.DeliveryRepository;
import ru.yandex.practicum.sht.commerce.ia.dto.delivery.DeliveryDto;
import ru.yandex.practicum.sht.commerce.ia.dto.delivery.dict.DeliveryState;
import ru.yandex.practicum.sht.commerce.ia.dto.order.OrderDto;
import ru.yandex.practicum.sht.commerce.ia.dto.warehouse.ShippedToDeliveryRequest;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@Transactional
public class DeliveryServiceImpl implements DeliveryService {

    private static final BigDecimal BASE_COEFF = BigDecimal.valueOf(5.0);
    private static final BigDecimal ADDRESS_1_COEFF = BigDecimal.valueOf(1);
    private static final BigDecimal ADDRESS_2_COEFF = BigDecimal.valueOf(2);
    private static final BigDecimal FRAGILE_COEFF = BigDecimal.valueOf(0.2);
    private static final BigDecimal WEIGHT_COEFF = BigDecimal.valueOf(0.3);
    private static final BigDecimal VOLUME_COEFF = BigDecimal.valueOf(0.2);
    private static final BigDecimal STREET_COEFF = BigDecimal.valueOf(0.2);

    private final DeliveryRepository deliveryRepository;
    private final DeliveryMapper deliveryMapper;
    private final OrderClient orderClient;
    private final WarehouseClient warehouseClient;

    public DeliveryServiceImpl(DeliveryMapper deliveryMapper, DeliveryRepository deliveryRepository, OrderClient orderClient, WarehouseClient warehouseClient) {
        this.deliveryMapper = deliveryMapper;
        this.deliveryRepository = deliveryRepository;
        this.orderClient = orderClient;
        this.warehouseClient = warehouseClient;
    }

    @Override
    public DeliveryDto createDelivery(DeliveryDto request) {
        Delivery delivery = deliveryMapper.mapToDelivery(request);
        delivery.setDeliveryState(DeliveryState.CREATED);
        return deliveryMapper.mapToDeliveryDto(deliveryRepository.save(delivery));
    }

    @Override
    public void successDelivery(UUID orderId) {
        Delivery delivery = getDeliveryByOrderId(orderId);
        delivery.setDeliveryState(DeliveryState.DELIVERED);
        deliveryRepository.save(delivery);
        orderClient.deliverOrder(orderId);
    }

    private Delivery getDeliveryByOrderId(UUID orderId) {
        return deliveryRepository.findByOrderId(orderId)
                .orElseThrow(() -> new NoDeliveryFoundException(String.format("There is no delivery with orderId %s", orderId)));
    }

    @Override
    public void pickDelivery(UUID orderId) {
        Delivery delivery = getDeliveryByOrderId(orderId);
        warehouseClient.shippedToDelivery(ShippedToDeliveryRequest.builder()
                .orderId(orderId)
                .deliveryId(delivery.getDeliveryId())
                .build());
        delivery.setDeliveryState(DeliveryState.IN_PROGRESS);
        deliveryRepository.save(delivery);
    }

    @Override
    public void failDelivery(UUID orderId) {
        Delivery delivery = getDeliveryByOrderId(orderId);
        delivery.setDeliveryState(DeliveryState.FAILED);
        deliveryRepository.save(delivery);
        orderClient.deliverOrderFailed(orderId);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal costDelivery(OrderDto request) {
        Delivery delivery = getDeliveryById(request.getDeliveryId());
        Address fromAddress = delivery.getFromAddress();
        Address toAddress = delivery.getToAddress();
        BigDecimal deliveryCost = BASE_COEFF;

        deliveryCost = switch (fromAddress.toString()) {
            case "ADDRESS_1" -> deliveryCost.multiply(ADDRESS_1_COEFF);
            case "ADDRESS_2" -> deliveryCost.add(deliveryCost.multiply(ADDRESS_2_COEFF));
            default -> deliveryCost;
        };

        if (request.isFragile()) {
            deliveryCost = deliveryCost.add(deliveryCost.multiply(FRAGILE_COEFF));
        }

        deliveryCost = deliveryCost.add(BigDecimal.valueOf(request.getDeliveryWeight()).multiply(WEIGHT_COEFF));
        deliveryCost = deliveryCost.add(BigDecimal.valueOf(request.getDeliveryVolume()).multiply(VOLUME_COEFF));

        if (!fromAddress.getStreet().equals(toAddress.getStreet())) {
            deliveryCost = deliveryCost.add(deliveryCost.multiply(STREET_COEFF));
        }

        return deliveryCost;
    }

    private Delivery getDeliveryById(UUID deliveryId) {
        return deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new NoDeliveryFoundException(String.format("There is no delivery with deliveryId %s", deliveryId)));
    }
}
