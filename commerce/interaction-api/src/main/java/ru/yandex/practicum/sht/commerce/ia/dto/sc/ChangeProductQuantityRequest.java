package ru.yandex.practicum.sht.commerce.ia.dto.sc;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChangeProductQuantityRequest {
    @NotNull
    UUID productId;
    long newQuantity;
}
