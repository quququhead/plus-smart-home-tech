package ru.yandex.practicum.sht.commerce.delivery.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.yandex.practicum.sht.commerce.delivery.model.Delivery;
import ru.yandex.practicum.sht.commerce.ia.dto.delivery.DeliveryDto;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface DeliveryMapper {
    DeliveryDto mapToDeliveryDto(Delivery delivery);

    Delivery mapToDelivery(DeliveryDto dto);
}
