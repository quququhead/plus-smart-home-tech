package ru.yandex.practicum.sht.commerce.warehouse.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "order_booking")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderBooking {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID orderBookingId;
    private UUID orderId;
    private UUID deliveryId;
    boolean fragile;
    double deliveryVolume;
    double deliveryWeight;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "booking_product", joinColumns = @JoinColumn(name = "order_booking_id"))
    @MapKeyColumn(name = "product_id")
    @Column(name = "quantity")
    private Map<UUID, Long> products;
}
