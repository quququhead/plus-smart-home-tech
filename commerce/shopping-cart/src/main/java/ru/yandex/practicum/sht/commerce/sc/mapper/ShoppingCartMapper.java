package ru.yandex.practicum.sht.commerce.sc.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.yandex.practicum.sht.commerce.ia.dto.sc.ShoppingCartDto;
import ru.yandex.practicum.sht.commerce.sc.model.ShoppingCart;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ShoppingCartMapper {
    ShoppingCartDto mapToShoppingCartDto(ShoppingCart cart);
}
