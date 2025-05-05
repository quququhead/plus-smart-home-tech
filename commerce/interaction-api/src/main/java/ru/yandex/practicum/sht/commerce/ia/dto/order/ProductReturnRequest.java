package ru.yandex.practicum.sht.commerce.ia.dto.order;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Map;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductReturnRequest {
    @NotNull
    UUID orderId;

    @NotNull
    Map<UUID, Long> products;
}
