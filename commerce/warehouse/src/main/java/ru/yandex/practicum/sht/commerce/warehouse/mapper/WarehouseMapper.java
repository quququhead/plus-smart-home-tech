package ru.yandex.practicum.sht.commerce.warehouse.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.yandex.practicum.sht.commerce.ia.dto.warehouse.NewProductInWarehouseRequest;
import ru.yandex.practicum.sht.commerce.warehouse.model.WarehouseProduct;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface WarehouseMapper {
    WarehouseProduct mapToWarehouseProduct(NewProductInWarehouseRequest warehouseRequest);
}
