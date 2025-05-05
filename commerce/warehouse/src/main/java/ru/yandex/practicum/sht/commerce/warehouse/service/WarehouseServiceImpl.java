package ru.yandex.practicum.sht.commerce.warehouse.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.sht.commerce.ia.dto.sc.ShoppingCartDto;
import ru.yandex.practicum.sht.commerce.ia.dto.ss.SetProductQuantityStateRequest;
import ru.yandex.practicum.sht.commerce.ia.dto.ss.dict.QuantityState;
import ru.yandex.practicum.sht.commerce.ia.dto.warehouse.*;
import ru.yandex.practicum.sht.commerce.warehouse.exception.NoOrderBookingFoundException;
import ru.yandex.practicum.sht.commerce.warehouse.exception.NoSpecifiedProductInWarehouseException;
import ru.yandex.practicum.sht.commerce.warehouse.exception.ProductInShoppingCartLowQuantityInWarehouse;
import ru.yandex.practicum.sht.commerce.warehouse.exception.SpecifiedProductAlreadyInWarehouseException;
import ru.yandex.practicum.sht.commerce.warehouse.feign.ShoppingStoreClient;
import ru.yandex.practicum.sht.commerce.warehouse.mapper.WarehouseMapper;
import ru.yandex.practicum.sht.commerce.warehouse.model.Dimension;
import ru.yandex.practicum.sht.commerce.warehouse.model.OrderBooking;
import ru.yandex.practicum.sht.commerce.warehouse.model.WarehouseProduct;
import ru.yandex.practicum.sht.commerce.warehouse.repository.OrderBookingRepository;
import ru.yandex.practicum.sht.commerce.warehouse.repository.WarehouseProductRepository;

import java.security.SecureRandom;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
public class WarehouseServiceImpl implements WarehouseService {

    private static final String[] ADDRESSES =
            new String[] {"ADDRESS_1", "ADDRESS_2"};
    private static final String CURRENT_ADDRESS =
            ADDRESSES[Random.from(new SecureRandom()).nextInt(0, 1)];

    private final WarehouseProductRepository warehouseProductRepository;
    private final OrderBookingRepository orderBookingRepository;
    private final WarehouseMapper warehouseMapper;
    private final ShoppingStoreClient shoppingStoreClient;

    public WarehouseServiceImpl(WarehouseProductRepository warehouseProductRepository, OrderBookingRepository orderBookingRepository, WarehouseMapper warehouseMapper, ShoppingStoreClient shoppingStoreClient) {
        this.warehouseProductRepository = warehouseProductRepository;
        this.orderBookingRepository = orderBookingRepository;
        this.warehouseMapper = warehouseMapper;
        this.shoppingStoreClient = shoppingStoreClient;
    }

    @Override
    public void addProductToWarehouse(NewProductInWarehouseRequest warehouseRequest) {
        if (warehouseProductRepository.existsById(warehouseRequest.getProductId())) {
            throw new SpecifiedProductAlreadyInWarehouseException(String.format("The product %s is already in warehouse", warehouseRequest.getProductId()));
        }
        warehouseProductRepository.save(warehouseMapper.mapToWarehouseProduct(warehouseRequest));
    }

    @Override
    public void shippedToDelivery(ShippedToDeliveryRequest request) {
        OrderBooking orderBooking = getOrderBookingByOrderId(request.getOrderId());
        orderBooking.setDeliveryId(request.getDeliveryId());
        orderBookingRepository.save(orderBooking);
    }

    private OrderBooking getOrderBookingByOrderId(UUID orderId) {
        return orderBookingRepository.findByOrderId(orderId)
                .orElseThrow(() -> new NoOrderBookingFoundException(String.format("There is no booking order with orderId %s", orderId)));
    }

    @Override
    public void acceptReturn(Map<UUID, Long> products) {
        Map<UUID, WarehouseProduct> warehouseProducts = warehouseProductRepository.findAllById(products.keySet()).stream()
                .collect(Collectors.toMap(WarehouseProduct::getProductId, Function.identity()));
        products.forEach((key, amount) -> {
            if (!warehouseProducts.containsKey(key)) {
                throw new NoSpecifiedProductInWarehouseException(String.format("The product %s is not in warehouse", key));
            }
            WarehouseProduct warehouseProduct = warehouseProducts.get(key);
            long newQuantity = warehouseProduct.getQuantity() + amount;
            warehouseProduct.setQuantity(newQuantity);
            shoppingStoreClient.updateProductQuantityState(getProductQuantityStateRequest(newQuantity, key));
        });
        warehouseProductRepository.saveAll(warehouseProducts.values());
    }

    private SetProductQuantityStateRequest getProductQuantityStateRequest(long quantity, UUID productId) {
        QuantityState quantityState;
        if (quantity > 100) {
            quantityState = QuantityState.MANY;
        } else if (quantity > 10) {
            quantityState = QuantityState.ENOUGH;
        } else if (quantity > 0) {
            quantityState = QuantityState.FEW;
        } else {
            quantityState = QuantityState.ENDED;
        }
        return new SetProductQuantityStateRequest(productId, quantityState);
    }

    @Override
    @Transactional(readOnly = true)
    public BookedProductsDto checkProductQuantity(ShoppingCartDto shoppingCartDto) {
        Map<UUID, Long> shoppingCartProducts = shoppingCartDto.getProducts();
        Map<UUID, WarehouseProduct> warehouseProducts = warehouseProductRepository.findAllById(shoppingCartProducts.keySet()).stream()
                .collect(Collectors.toMap(
                        WarehouseProduct::getProductId, Function.identity()
                ));
        BookedProductsDto bookedProductsDto = BookedProductsDto.builder()
                .deliveryWeight(0)
                .deliveryVolume(0)
                .fragile(false)
                .build();
        shoppingCartProducts.entrySet().forEach(product -> processProduct(product, warehouseProducts, bookedProductsDto, false));
        return bookedProductsDto;
    }

    @Override
    public BookedProductsDto assemblyProducts(AssemblyProductsForOrderRequest request) {
        Map<UUID, Long> products = request.getProducts();
        Map<UUID, WarehouseProduct> warehouseProducts = warehouseProductRepository.findAllById(products.keySet()).stream()
                .collect(Collectors.toMap(
                        WarehouseProduct::getProductId, Function.identity()
                ));
        BookedProductsDto bookedProducts = BookedProductsDto.builder()
                .deliveryWeight(0)
                .deliveryVolume(0)
                .fragile(false)
                .build();
        products.entrySet().forEach(product -> processProduct(product, warehouseProducts, bookedProducts, true));
        orderBookingRepository.save(OrderBooking.builder()
                .orderId(request.getOrderId())
                .products(products)
                .deliveryVolume(bookedProducts.getDeliveryVolume())
                .deliveryWeight(bookedProducts.getDeliveryWeight())
                .fragile(bookedProducts.isFragile())
                .build());
        return bookedProducts;
    }

    private void processProduct(Map.Entry<UUID, Long> product, Map<UUID, WarehouseProduct> warehouseProducts, BookedProductsDto bookedProductsDto, boolean isBooking) {
        UUID id = product.getKey();
        WarehouseProduct warehouseProduct = warehouseProducts.get(id);
        if (warehouseProduct == null) {
            throw new NoSpecifiedProductInWarehouseException(String.format("The product %s is not in warehouse", id));
        }
        if (warehouseProduct.getQuantity() >= product.getValue()) {
            double weight = bookedProductsDto.getDeliveryWeight() + warehouseProduct.getWeight() * product.getValue();
            bookedProductsDto.setDeliveryWeight(weight);
            double volume = bookedProductsDto.getDeliveryVolume() + calculateVolume(warehouseProduct.getDimension()) * product.getValue();
            bookedProductsDto.setDeliveryVolume(volume);
            if (warehouseProduct.isFragile()) {
                bookedProductsDto.setFragile(true);
            }
            if (isBooking) {
                warehouseProduct.setQuantity(warehouseProduct.getQuantity() - product.getValue());
                warehouseProductRepository.save(warehouseProduct);
            }
        } else {
            throw new ProductInShoppingCartLowQuantityInWarehouse(String.format("Available quantity of product %s in warehouse %s", id, warehouseProduct.getQuantity()));
        }
    }

    private double calculateVolume(Dimension dimension) {
        return dimension.getWidth() * dimension.getHeight() * dimension.getDepth();
    }

    @Override
    public void acceptProductToWarehouse(AddProductToWarehouseRequest warehouseRequest) {
        WarehouseProduct product = getWarehouseProduct(warehouseRequest);
        long newQuantity = product.getQuantity() + warehouseRequest.getQuantity();
        product.setQuantity(newQuantity);
        warehouseProductRepository.save(product);
        shoppingStoreClient.updateProductQuantityState(getProductQuantityStateRequest(newQuantity, product.getProductId()));
    }

    private WarehouseProduct getWarehouseProduct(AddProductToWarehouseRequest warehouseRequest) {
        return warehouseProductRepository.findById(warehouseRequest.getProductId())
                .orElseThrow(() -> new NoSpecifiedProductInWarehouseException(String.format("The product %s is not in warehouse", warehouseRequest.getProductId())));
    }

    @Override
    public AddressDto getWarehouseAddress() {
        return AddressDto.builder()
                .country(CURRENT_ADDRESS)
                .city(CURRENT_ADDRESS)
                .street(CURRENT_ADDRESS)
                .house(CURRENT_ADDRESS)
                .flat(CURRENT_ADDRESS)
                .build();
    }
}
