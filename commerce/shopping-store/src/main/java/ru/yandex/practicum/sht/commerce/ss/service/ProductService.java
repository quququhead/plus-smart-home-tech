package ru.yandex.practicum.sht.commerce.ss.service;

import org.springframework.data.domain.Pageable;
import ru.yandex.practicum.sht.commerce.ia.dto.ss.ProductDto;
import ru.yandex.practicum.sht.commerce.ia.dto.ss.SetProductQuantityStateRequest;
import ru.yandex.practicum.sht.commerce.ia.dto.ss.dict.ProductCategory;

import java.util.List;
import java.util.UUID;

public interface ProductService {
    List<ProductDto> getSortedProducts(ProductCategory category, Pageable pageable);

    ProductDto createProduct(ProductDto productDto);

    ProductDto updateProduct(ProductDto productDto);

    boolean deleteProduct(UUID productId);

    boolean updateProductQuantityState(SetProductQuantityStateRequest quantityState);

    ProductDto getProduct(UUID productId);
}
