package com.cwz.electronic.store.dtos;

import com.cwz.electronic.store.entities.CartItem;
import com.cwz.electronic.store.entities.User;
import lombok.*;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartDto {
    private String cartId;
    private Date createdAt;
//    private int totalItems;
//    private Long totalPrice;
    private UserDto user;
    private List<CartItemDto> items = new ArrayList<>();
}
