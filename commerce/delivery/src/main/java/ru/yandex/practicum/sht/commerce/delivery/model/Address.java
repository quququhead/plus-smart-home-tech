package ru.yandex.practicum.sht.commerce.delivery.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Entity
@Table(name = "address")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID addressId;
    String country;
    String city;
    String street;
    String house;
    String flat;
}
