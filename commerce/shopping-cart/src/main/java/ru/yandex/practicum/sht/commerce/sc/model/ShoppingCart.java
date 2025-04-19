package ru.yandex.practicum.sht.commerce.sc.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "cart")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ShoppingCart {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID shoppingCartId;

    @Column(nullable = false)
    String username;

    boolean active = true;

    @ElementCollection
    @CollectionTable(name = "product", joinColumns = @JoinColumn(name = "cart_id"))
    @MapKeyColumn(name = "product_id")
    @Column(name = "quantity")
    Map<UUID, Long> products;
}
