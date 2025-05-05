package ru.yandex.practicum.sht.commerce.delivery.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.yandex.practicum.sht.commerce.delivery.model.Address;
import ru.yandex.practicum.sht.commerce.ia.dto.warehouse.AddressDto;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AddressMapper {
    Address maptoAddress(AddressDto address);
}
