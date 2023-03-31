package com.cwz.electronic.store.services;

import com.cwz.electronic.store.dtos.CreateOrderRequest;
import com.cwz.electronic.store.dtos.OrderDto;
import com.cwz.electronic.store.dtos.PageableResponse;
import com.cwz.electronic.store.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderService{

    //create order
    OrderDto createOrder(CreateOrderRequest orderRequest);
    //update order
    OrderDto updateOrder(OrderDto orderDto, String orderId);
    //delete order
    void removeOrder(String oderId);

    //get orders of user
    List<OrderDto> getOrdersOfUser(String userId);

    //get all orders
    PageableResponse<OrderDto> getAllOrders(int pageNumber, int pageSize, String sortBy, String sortDir);

}
