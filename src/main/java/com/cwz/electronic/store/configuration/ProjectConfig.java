package com.cwz.electronic.store.configuration;

import com.cwz.electronic.store.dtos.CartDto;
import com.cwz.electronic.store.dtos.CartItemDto;
import com.cwz.electronic.store.entities.Cart;
import com.cwz.electronic.store.entities.CartItem;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProjectConfig {

//    @Bean
//    public ModelMapper model(){
//        return new ModelMapper();
//    }
//}
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // Add mapping configuration for CartItem to CartItemDto
        modelMapper.typeMap(CartItem.class, CartItemDto.class)
                .addMapping(CartItem::getProduct, CartItemDto::setProduct)
                .addMapping(CartItem::getQuantity, CartItemDto::setQuantity)
                .addMapping(CartItem::getTotalPrice, CartItemDto::setTotalPrice);

        modelMapper.createTypeMap(Cart.class, CartDto.class)
                .addMapping(Cart::getCartId, CartDto::setCartId)
                .addMapping(Cart::getCreatedAt, CartDto::setCreatedAt)
                .addMapping(Cart::getUser, CartDto::setUser)
                .addMapping(Cart::getItems, CartDto::setItems);

        return modelMapper;
    }
}

