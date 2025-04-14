package ru.yandex.practicum.sht.commerce.sc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.sht.commerce.sc.model.ShoppingCart;

import java.util.Optional;
import java.util.UUID;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, UUID> {
    Optional<ShoppingCart> findByUsername(String username);
}
