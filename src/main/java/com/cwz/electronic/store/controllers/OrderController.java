package com.cwz.electronic.store.controllers;

import com.cwz.electronic.store.dtos.ApiResponseMessage;
import com.cwz.electronic.store.dtos.CreateOrderRequest;
import com.cwz.electronic.store.dtos.OrderDto;
import com.cwz.electronic.store.dtos.PageableResponse;
import com.cwz.electronic.store.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<OrderDto> handlerCreateOrder(@RequestBody CreateOrderRequest orderRequest){
        OrderDto order = orderService.createOrder(orderRequest);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    @PutMapping("/update/{orderId}")
    public ResponseEntity<OrderDto> handlerUpdateOrder(
            @RequestBody OrderDto orderDto,
            @PathVariable String orderId){
        OrderDto order = orderService.updateOrder(orderDto, orderId);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{orderId}")
    public ResponseEntity<ApiResponseMessage> removeOrder(@PathVariable String orderId) {
        orderService.removeOrder(orderId);
        ApiResponseMessage responseMessage = ApiResponseMessage.builder()
                .status(HttpStatus.OK)
                .message("Your order is removed !!")
                .success(true)
                .build();
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }
    @GetMapping("/users/{userId}")
    public ResponseEntity<List<OrderDto>> getOrdersOfUser(@PathVariable String userId) {
        List<OrderDto> ordersOfUser = orderService.getOrdersOfUser(userId);
        return new ResponseEntity<>(ordersOfUser, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<PageableResponse<OrderDto>> getOrders(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "orderDate", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "desc", required = false) String sortDir
    ) {
        PageableResponse<OrderDto> orders = orderService.getAllOrders(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }


}
