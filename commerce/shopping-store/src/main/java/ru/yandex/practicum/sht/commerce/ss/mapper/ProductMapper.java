package ru.yandex.practicum.sht.commerce.ss.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.yandex.practicum.sht.commerce.ia.dto.ss.ProductDto;
import ru.yandex.practicum.sht.commerce.ss.model.Product;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductMapper {
    ProductDto mapToProductDto(Product product);

    Product mapToProduct(ProductDto productDto);
}
