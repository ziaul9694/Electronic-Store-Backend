package com.cwz.electronic.store.controllers;

import com.cwz.electronic.store.dtos.AddItemsToCartRequest;
import com.cwz.electronic.store.dtos.ApiResponseMessage;
import com.cwz.electronic.store.dtos.CartDto;
import com.cwz.electronic.store.entities.Cart;
import com.cwz.electronic.store.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carts")
public class CartController {
    @Autowired
    private CartService cartService;
    //add items to cart
    @PostMapping("/create/{userId}")
    public ResponseEntity<CartDto> addItemToCart(
            @RequestBody AddItemsToCartRequest request,
            @PathVariable String userId){
        CartDto cartDto = cartService.addItemsToCart(userId, request);
        return new ResponseEntity<>(cartDto, HttpStatus.CREATED);
    }
    //remove cart item from cart
    @DeleteMapping("/delete/{userId}/cart-item/{cartItemId}")
    public ResponseEntity<ApiResponseMessage> removeItemFromCart(
            @PathVariable String userId,
            @PathVariable int cartItemId
    ){
       cartService.removeItemFromCart(userId, cartItemId);
        ApiResponseMessage message = ApiResponseMessage.builder()
                .message("Successfully deleted.")
                .success(true)
                .status(HttpStatus.OK)
                .build();
        return new ResponseEntity<>(message,HttpStatus.OK);
    }
    //clear cart
    @DeleteMapping("/clear/{userId}")
    public ResponseEntity<ApiResponseMessage> clearCart(
            @PathVariable String userId
    ){
        cartService.clearCart(userId);
        ApiResponseMessage message = ApiResponseMessage.builder()
                .message("Successfully deleted.")
                .success(true)
                .status(HttpStatus.OK)
                .build();
        return new ResponseEntity<>(message,HttpStatus.OK);
    }
    //get single cart
    @GetMapping("/cart/{userId}")
    public ResponseEntity<CartDto> getSingleCart(
            @PathVariable String userId){
        CartDto cartDto = cartService.getCartByUser(userId);
        return new ResponseEntity<>(cartDto, HttpStatus.OK);
    }
}
