package ru.yandex.practicum.sht.commerce.ia.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.sht.commerce.ia.dto.ss.ProductDto;
import ru.yandex.practicum.sht.commerce.ia.dto.ss.SetProductQuantityStateRequest;
import ru.yandex.practicum.sht.commerce.ia.dto.ss.dict.ProductCategory;

import java.util.List;
import java.util.UUID;

public interface ShoppingStoreOperations {
    @GetMapping
    List<ProductDto> getSortedProducts(@RequestParam @NotNull ProductCategory category, Pageable pageable);

    @PutMapping
    ProductDto createProduct(@RequestBody @Valid ProductDto productDto);

    @PostMapping
    ProductDto updateProduct(@RequestBody @Valid ProductDto productDto);

    @PostMapping("/removeProductFromStore")
    boolean deleteProduct(@RequestBody @NotNull UUID productId);

    @PostMapping("/quantityState")
    boolean updateProductQuantityState(@RequestBody @Valid SetProductQuantityStateRequest quantityState);

    @GetMapping("/{productId}")
    ProductDto getProduct(@PathVariable @NotNull UUID productId);

}