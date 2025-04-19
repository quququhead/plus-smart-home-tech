package ru.yandex.practicum.sht.commerce.ss.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.sht.commerce.ia.dto.ss.dict.ProductCategory;
import ru.yandex.practicum.sht.commerce.ss.model.Product;

import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    Page<Product> findAllByProductCategory(ProductCategory category, Pageable pageable);
}
