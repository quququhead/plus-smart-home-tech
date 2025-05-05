package ru.yandex.practicum.sht.commerce.ia.dto.warehouse;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ShippedToDeliveryRequest {
    @NotNull
    UUID orderId;

    @NotNull
    UUID deliveryId;
}
