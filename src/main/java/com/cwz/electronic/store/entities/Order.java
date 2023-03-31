package com.cwz.electronic.store.entities;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "orders")
public class Order {
    @Id
    private String orderId;
    private String orderStatus;
    private String paymentStatus;
    private int orderAmount;
    @Column(length = 1000)
    private String billingAddress;
    private String billingPhone;
    private String billingName;
    private Date orderDate;
    private Date deliverDate;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;
    @OneToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL,mappedBy = "order")
    private List<OrderItem> orderItems = new ArrayList<>();


}
