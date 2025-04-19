package ru.yandex.practicum.sht.commerce.ia.dto.warehouse;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookedProductsDto {
    double deliveryWeight;
    double deliveryVolume;
    boolean fragile;
}
