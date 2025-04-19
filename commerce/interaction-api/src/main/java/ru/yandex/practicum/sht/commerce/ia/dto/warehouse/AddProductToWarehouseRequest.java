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
public class AddProductToWarehouseRequest {
    @NotNull
    UUID productId;
    @Min(1)
    long quantity;
}
