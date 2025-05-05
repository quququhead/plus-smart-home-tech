package ru.yandex.practicum.sht.commerce.warehouse.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.yandex.practicum.sht.commerce.ia.dto.warehouse.AssemblyProductsForOrderRequest;
import ru.yandex.practicum.sht.commerce.warehouse.model.OrderBooking;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderBookingMapper {
    OrderBooking mapToOrderBooking(AssemblyProductsForOrderRequest request);
}
