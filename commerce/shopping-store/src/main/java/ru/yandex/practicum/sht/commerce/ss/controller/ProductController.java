package ru.yandex.practicum.sht.commerce.ss.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.sht.commerce.ia.controller.ShoppingStoreOperations;
import ru.yandex.practicum.sht.commerce.ia.dto.ss.ProductDto;
import ru.yandex.practicum.sht.commerce.ia.dto.ss.SetProductQuantityStateRequest;
import ru.yandex.practicum.sht.commerce.ia.dto.ss.dict.ProductCategory;
import ru.yandex.practicum.sht.commerce.ss.service.ProductService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/shopping-store")
public class ProductController implements ShoppingStoreOperations {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<ProductDto> getSortedProducts(@RequestParam @NotNull ProductCategory category, Pageable pageable) {
        return productService.getSortedProducts(category, pageable);
    }

    @PutMapping
    public ProductDto createProduct(@RequestBody @Valid ProductDto productDto) {
        return productService.createProduct(productDto);
    }

    @PostMapping
    public ProductDto updateProduct(@RequestBody @Valid ProductDto productDto) {
        return productService.updateProduct(productDto);
    }

    @PostMapping("/removeProductFromStore")
    public boolean deleteProduct(@RequestBody @NotNull UUID productId) {
        return productService.deleteProduct(productId);
    }

    @PostMapping("/quantityState")
    public boolean updateProductQuantityState(@RequestBody @Valid SetProductQuantityStateRequest quantityState) {
        return productService.updateProductQuantityState(quantityState);
    }

    @GetMapping("/{productId}")
    public ProductDto getProduct(@PathVariable @NotNull UUID productId) {
        return productService.getProduct(productId);
    }
}
