package ru.yandex.practicum.sht.commerce.ia.dto.warehouse;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewProductInWarehouseRequest {
    @NotNull
    UUID productId;
    boolean fragile;
    @NotNull
    DimensionDto dimension;
    @Min(1)
    double weight;
}
