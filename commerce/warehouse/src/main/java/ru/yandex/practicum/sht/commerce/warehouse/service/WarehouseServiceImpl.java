package ru.yandex.practicum.sht.commerce.warehouse.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.sht.commerce.ia.dto.sc.ShoppingCartDto;
import ru.yandex.practicum.sht.commerce.ia.dto.ss.SetProductQuantityStateRequest;
import ru.yandex.practicum.sht.commerce.ia.dto.ss.dict.QuantityState;
import ru.yandex.practicum.sht.commerce.ia.dto.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.sht.commerce.ia.dto.warehouse.AddressDto;
import ru.yandex.practicum.sht.commerce.ia.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.sht.commerce.ia.dto.warehouse.NewProductInWarehouseRequest;
import ru.yandex.practicum.sht.commerce.warehouse.exception.NoSpecifiedProductInWarehouseException;
import ru.yandex.practicum.sht.commerce.warehouse.exception.ProductInShoppingCartLowQuantityInWarehouse;
import ru.yandex.practicum.sht.commerce.warehouse.exception.SpecifiedProductAlreadyInWarehouseException;
import ru.yandex.practicum.sht.commerce.warehouse.feign.ShoppingStoreClient;
import ru.yandex.practicum.sht.commerce.warehouse.mapper.WarehouseMapper;
import ru.yandex.practicum.sht.commerce.warehouse.model.WarehouseProduct;
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
    private final WarehouseMapper warehouseMapper;
    private final ShoppingStoreClient shoppingStoreClient;

    public WarehouseServiceImpl(ShoppingStoreClient shoppingStoreClient, WarehouseProductRepository warehouseProductRepository, WarehouseMapper warehouseMapper) {
        this.shoppingStoreClient = shoppingStoreClient;
        this.warehouseProductRepository = warehouseProductRepository;
        this.warehouseMapper = warehouseMapper;
    }

    @Override
    public void addProductToWarehouse(NewProductInWarehouseRequest warehouseRequest) {
        if (warehouseProductRepository.existsById(warehouseRequest.getProductId())) {
            throw new SpecifiedProductAlreadyInWarehouseException(String.format("The product %s is already in warehouse", warehouseRequest.getProductId()));
        }
        warehouseProductRepository.save(warehouseMapper.mapToWarehouseProduct(warehouseRequest));
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
        shoppingCartProducts.entrySet().forEach(product -> processProduct(product, warehouseProducts, bookedProductsDto));
        return bookedProductsDto;
    }

    private void processProduct(Map.Entry<UUID, Long> product, Map<UUID, WarehouseProduct> warehouseProducts, BookedProductsDto bookedProductsDto) {
        UUID id = product.getKey();
        WarehouseProduct warehouseProduct = warehouseProducts.get(id);
        if (warehouseProduct == null) {
            throw new NoSpecifiedProductInWarehouseException(String.format("The product %s is not in warehouse", id));
        }
        if (warehouseProduct.getQuantity() >= product.getValue()) {
            double weight = bookedProductsDto.getDeliveryWeight() + warehouseProduct.getWeight() * product.getValue();
            bookedProductsDto.setDeliveryWeight(weight);
            double volume = bookedProductsDto.getDeliveryVolume() + warehouseProduct.getDimension().getWidth() * warehouseProduct.getDimension().getHeight() * warehouseProduct.getDimension().getDepth() * product.getValue();
            bookedProductsDto.setDeliveryVolume(volume);
            if (warehouseProduct.isFragile()) {
                bookedProductsDto.setFragile(true);
            }
        } else {
            throw new ProductInShoppingCartLowQuantityInWarehouse(String.format("Available quantity of product %s in warehouse %s", id, warehouseProduct.getQuantity()));
        }
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
