package ru.yandex.practicum.sht.commerce.sc.service;

import feign.FeignException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.sht.commerce.ia.dto.sc.ChangeProductQuantityRequest;
import ru.yandex.practicum.sht.commerce.ia.dto.sc.ShoppingCartDto;
import ru.yandex.practicum.sht.commerce.sc.exception.DeactivatedShoppingCartException;
import ru.yandex.practicum.sht.commerce.sc.exception.NoProductsInShoppingCartException;
import ru.yandex.practicum.sht.commerce.sc.exception.NotAuthorizedUserException;
import ru.yandex.practicum.sht.commerce.sc.feign.WarehouseClient;
import ru.yandex.practicum.sht.commerce.sc.mapper.ShoppingCartMapper;
import ru.yandex.practicum.sht.commerce.sc.model.ShoppingCart;
import ru.yandex.practicum.sht.commerce.sc.repository.ShoppingCartRepository;

import java.util.*;

@Service
@Transactional
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final WarehouseClient warehouseClient;

    public ShoppingCartServiceImpl(ShoppingCartRepository shoppingCartRepository, ShoppingCartMapper shoppingCartMapper, WarehouseClient warehouseClient) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.shoppingCartMapper = shoppingCartMapper;
        this.warehouseClient = warehouseClient;
    }

    @Override
    public ShoppingCartDto getShoppingCart(String username) {
        checkUserIsAuthorized(username);
        ShoppingCart shoppingCart = getUserShoppingCart(username);
        return shoppingCartMapper.mapToShoppingCartDto(shoppingCart);
    }

    private void checkUserIsAuthorized(String username) {
        if (username.isBlank()) {
            throw new NotAuthorizedUserException("Username is blank");
        }
    }

    private ShoppingCart getUserShoppingCart(String username) {
        return shoppingCartRepository.findByUsername(username)
                .orElseGet(() -> shoppingCartRepository.save(createShoppingCart(username)));
    }

    private ShoppingCart createShoppingCart(String username) {
        return ShoppingCart.builder()
                .username(username)
                .active(true)
                .products(new HashMap<>())
                .build();
    }

    @Override
    public ShoppingCartDto addProductsToShoppingCart(String username, Map<UUID, Long> products) {
        checkUserIsAuthorized(username);
        ShoppingCart shoppingCart = getUserShoppingCart(username);
        checkShoppingCartIsActive(shoppingCart);
        Map<UUID, Long> oldProducts = shoppingCart.getProducts();
        oldProducts.putAll(products);
        warehouseClient.checkProductQuantity(shoppingCartMapper.mapToShoppingCartDto(shoppingCart));
        return shoppingCartMapper.mapToShoppingCartDto(shoppingCartRepository.save(shoppingCart));
    }

    private void checkShoppingCartIsActive(ShoppingCart shoppingCart) {
        if (!shoppingCart.isActive()) {
            throw new DeactivatedShoppingCartException(String.format("The shopping cart of user %s isn't active", shoppingCart.getUsername()));
        }
    }

    @Override
    public void deactivateShoppingCart(String username) {
        checkUserIsAuthorized(username);
        ShoppingCart shoppingCart = getUserShoppingCart(username);
        checkShoppingCartIsActive(shoppingCart);
        shoppingCart.setActive(false);
        shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public ShoppingCartDto removeProductsFromShoppingCart(String username, Set<UUID> products) {
        checkUserIsAuthorized(username);
        ShoppingCart shoppingCart = getUserShoppingCart(username);
        checkShoppingCartIsActive(shoppingCart);
        Map<UUID, Long> oldProducts = shoppingCart.getProducts();
        products.forEach(productId -> {
            if (!oldProducts.containsKey(productId)) {
                throw new NoProductsInShoppingCartException(String.format("The shopping cart of user %s does not exist product with id %s", username, productId));
            }
            oldProducts.remove(productId);
        });
        return shoppingCartMapper.mapToShoppingCartDto(shoppingCartRepository.save(shoppingCart));
    }

    @Override
    public ShoppingCartDto changeProductQuantity(String username, ChangeProductQuantityRequest productQuantity) {
        checkUserIsAuthorized(username);
        ShoppingCart shoppingCart = getUserShoppingCart(username);
        checkShoppingCartIsActive(shoppingCart);
        Map<UUID, Long> oldProducts = shoppingCart.getProducts();
        if (!oldProducts.containsKey(productQuantity.getProductId())) {
            throw new NoProductsInShoppingCartException(String.format("The shopping cart of user %s does not exist product with id %s", username, productQuantity.getProductId()));
        }
        oldProducts.put(productQuantity.getProductId(), productQuantity.getNewQuantity());
        warehouseClient.checkProductQuantity(shoppingCartMapper.mapToShoppingCartDto(shoppingCart));
        return shoppingCartMapper.mapToShoppingCartDto(shoppingCartRepository.save(shoppingCart));
    }
}
