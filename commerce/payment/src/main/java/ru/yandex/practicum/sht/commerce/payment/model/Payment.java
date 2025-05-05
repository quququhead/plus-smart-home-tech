package ru.yandex.practicum.sht.commerce.payment.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.sht.commerce.ia.dto.payment.PaymentState;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "payment")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID paymentId;
    UUID orderId;
    BigDecimal totalPayment;
    BigDecimal deliveryTotal;
    BigDecimal feeTotal;

    @Enumerated(EnumType.STRING)
    PaymentState paymentState;
}
