package ru.yandex.practicum.sht.commerce.ss.service;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.sht.commerce.ia.dto.ss.ProductDto;
import ru.yandex.practicum.sht.commerce.ia.dto.ss.SetProductQuantityStateRequest;
import ru.yandex.practicum.sht.commerce.ia.dto.ss.dict.ProductCategory;
import ru.yandex.practicum.sht.commerce.ia.dto.ss.dict.ProductState;
import ru.yandex.practicum.sht.commerce.ss.exception.ProductNotFoundException;
import ru.yandex.practicum.sht.commerce.ss.mapper.ProductMapper;
import ru.yandex.practicum.sht.commerce.ss.model.Product;
import ru.yandex.practicum.sht.commerce.ss.repository.ProductRepository;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductServiceImpl(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDto> getSortedProducts(ProductCategory category, Pageable pageable) {
        List<Product> products = productRepository.findAllByProductCategory(category, pageable).getContent();
        return products.stream()
                .map(productMapper::mapToProductDto)
                .toList();
    }

    @Override
    public ProductDto createProduct(ProductDto productDto) {
        Product product = productRepository.save(productMapper.mapToProduct(productDto));
        return productMapper.mapToProductDto(product);
    }

    @Override
    public ProductDto updateProduct(ProductDto productDto) {
        if (!productRepository.existsById(productDto.getProductId())) {
            throw new ProductNotFoundException(String.format("Product with id %s does not exist" , productDto.getProductId()));
        }
        Product product = productRepository.save(productMapper.mapToProduct(productDto));
        return productMapper.mapToProductDto(product);
    }

    @Override
    public boolean deleteProduct(UUID productId) {
        Product product = receiveProduct(productId);
        product.setProductState(ProductState.DEACTIVATE);
        productRepository.save(product);
        return true;
    }

    private Product receiveProduct(UUID productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(String.format("Product with id %s does not exist", productId)));
    }

    @Override
    public boolean updateProductQuantityState(SetProductQuantityStateRequest quantityState) {
        Product product = receiveProduct(quantityState.getProductId());
        product.setQuantityState(quantityState.getQuantityState());
        productRepository.save(product);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDto getProduct(UUID productId) {
        Product product = receiveProduct(productId);
        return productMapper.mapToProductDto(product);
    }
}
