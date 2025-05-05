package ru.yandex.practicum.sht.commerce.ia.dto.order;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.sht.commerce.ia.dto.order.dict.OrderState;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDto {
    @NotNull
    UUID orderId;

    UUID shoppingCartId;

    @NotNull
    Map<UUID, Long> products;

    UUID paymentId;

    UUID deliveryId;

    OrderState state;

    double deliveryWeight;

    double deliveryVolume;

    boolean fragile;

    BigDecimal totalPrice;

    BigDecimal deliveryPrice;

    BigDecimal productPrice;
}
