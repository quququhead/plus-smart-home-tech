package ru.yandex.practicum.sht.commerce.warehouse.model;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Entity
@Table(schema = "warehouse", name = "product")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WarehouseProduct {
    @Id
    UUID productId;
    boolean fragile;
    @Embedded
    Dimension dimension;
    double weight;
    long quantity;
}
