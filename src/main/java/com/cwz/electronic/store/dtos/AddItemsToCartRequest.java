package com.cwz.electronic.store.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddItemsToCartRequest {
    private String productId;
    private int quantity;
}
