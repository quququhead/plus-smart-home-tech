package ru.yandex.practicum.sht.commerce.ia.dto.ss;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.sht.commerce.ia.dto.ss.dict.QuantityState;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SetProductQuantityStateRequest {
    @NotNull
    UUID productId;
    @NotNull
    QuantityState quantityState;
}
