package com.cwz.electronic.store.services.Impl;

import com.cwz.electronic.store.dtos.AddItemsToCartRequest;
import com.cwz.electronic.store.dtos.CartDto;
import com.cwz.electronic.store.entities.Cart;
import com.cwz.electronic.store.entities.CartItem;
import com.cwz.electronic.store.entities.Product;
import com.cwz.electronic.store.entities.User;
import com.cwz.electronic.store.exception.BadApiRequest;
import com.cwz.electronic.store.exception.ResourceNotFoundException;
import com.cwz.electronic.store.repositories.CartItemRepository;
import com.cwz.electronic.store.repositories.CartRepository;
import com.cwz.electronic.store.repositories.ProductRepository;
import com.cwz.electronic.store.repositories.UserRepository;
import com.cwz.electronic.store.services.CartService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private CartItemRepository cartItemRepository;

    @Override
    public CartDto addItemsToCart(String userId, AddItemsToCartRequest request) {

        int quantity = request.getQuantity();
        String productId = request.getProductId();

        if (quantity <= 0) {
            throw new BadApiRequest("Bad request.");
        }

        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("product do not belong in our store."));

        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Invalid User"));

        Cart cart = null;
        try {
            cart = cartRepository.findByUser(user).get();

        } catch (NoSuchElementException e) {
            cart = new Cart();
            cart.setCartId(UUID.randomUUID().toString());
            cart.setCreatedAt(new Date());
        }
        AtomicReference<Boolean> updated = new AtomicReference<>(false);
        List<CartItem> items = cart.getItems();
        items = items.stream().map(cartItem -> {
            if (cartItem.getProduct().getProductId().equals(productId)) {
                cartItem.setQuantity(quantity);
                cartItem.setTotalPrice(quantity * product.getPrice());
                updated.set(true);
            }
            return cartItem;
        }).collect(Collectors.toList());
        cart.setItems(items);


        if (!updated.get()) {
            CartItem cartItem = CartItem.builder()
                    .quantity(quantity)
                    .totalPrice(quantity * product.getPrice())
                    .cart(cart)
                    .product(product)
                    .build();
            cart.getItems().add(cartItem);
        }
        cart.setUser(user);
        Cart updatedCart = cartRepository.save(cart);
        CartDto dto = mapper.map(updatedCart, CartDto.class);
        return dto;
    }

    @Override
    public void removeItemFromCart(String userId, int cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(() -> new ResourceNotFoundException("This Item is not available."));
        cartItemRepository.delete(cartItem);
    }
    @Override
    public void clearCart(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("user not found"));
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("cart not found."));
        cart.getItems().clear();
        cartRepository.save(cart);
    }

    @Override
    public CartDto getCartByUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("user not found"));
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("cart not found."));
        return mapper.map(cart,CartDto.class);
    }
}
