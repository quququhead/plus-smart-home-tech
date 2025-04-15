package ru.yandex.practicum.sht.commerce.ss.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.sht.commerce.ia.dto.ss.dict.ProductCategory;
import ru.yandex.practicum.sht.commerce.ia.dto.ss.dict.ProductState;
import ru.yandex.practicum.sht.commerce.ia.dto.ss.dict.QuantityState;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "product")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID productId;
    @Column(nullable = false)
    String productName;
    @Column(nullable = false)
    String description;
    String imageSrc;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    QuantityState quantityState;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    ProductState productState;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    ProductCategory productCategory;
    @Column(nullable = false)
    BigDecimal price;
}
