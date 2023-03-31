package com.cwz.electronic.store.services;

import com.cwz.electronic.store.dtos.AddItemsToCartRequest;
import com.cwz.electronic.store.dtos.CartDto;

public interface CartService {

    //add items to cart
    CartDto addItemsToCart(String userId, AddItemsToCartRequest request);

    //remove item from cart
    void removeItemFromCart(String userId, int cartItemId);
    //remove all items from cart
    void clearCart(String userId);

    CartDto getCartByUser(String userId);

}
