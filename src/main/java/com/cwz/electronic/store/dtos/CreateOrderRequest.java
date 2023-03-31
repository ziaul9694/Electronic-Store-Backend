package com.cwz.electronic.store.dtos;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateOrderRequest {
    @NotBlank(message = "Give me your cart ID.")
    private String cartId;
    @NotBlank(message = "Must give your UserID")
    private String userId;
    private String orderStatus = "PENDING";
    private String paymentStatus = "NOTPAID";
    @NotBlank(message = "What will be the billing address?")
    private String billingAddress;
    @NotBlank(message = "What will be your number to contact?")
    private String billingPhone;
    @NotBlank(message = "What will be your name on the bill?")
    private String billingName;
}
