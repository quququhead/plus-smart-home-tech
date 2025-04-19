package ru.yandex.practicum.sht.commerce.warehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.sht.commerce.warehouse.model.WarehouseProduct;

import java.util.UUID;

public interface WarehouseProductRepository extends JpaRepository<WarehouseProduct, UUID> {
}
