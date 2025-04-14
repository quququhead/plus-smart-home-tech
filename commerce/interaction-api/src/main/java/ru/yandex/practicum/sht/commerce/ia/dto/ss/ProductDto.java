package ru.yandex.practicum.sht.commerce.ia.dto.ss;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.sht.commerce.ia.dto.ss.dict.ProductCategory;
import ru.yandex.practicum.sht.commerce.ia.dto.ss.dict.ProductState;
import ru.yandex.practicum.sht.commerce.ia.dto.ss.dict.QuantityState;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductDto {
    UUID productId;
    @NotBlank
    String productName;
    @NotBlank
    String description;
    @NotEmpty
    String imageSrc;
    @NotNull
    QuantityState quantityState;
    @NotNull
    ProductState productState;
    @NotNull
    ProductCategory productCategory;
    @NotNull
    @Min(1)
    BigDecimal price;
}
