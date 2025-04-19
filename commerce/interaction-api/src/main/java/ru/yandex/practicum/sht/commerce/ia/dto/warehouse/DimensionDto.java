package ru.yandex.practicum.sht.commerce.ia.dto.warehouse;

import jakarta.validation.constraints.Min;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DimensionDto {
    @Min(1)
    double width;
    @Min(1)
    double height;
    @Min(1)
    double depth;
}
