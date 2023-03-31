package com.cwz.electronic.store.services.Impl;

import com.cwz.electronic.store.dtos.CreateOrderRequest;
import com.cwz.electronic.store.dtos.OrderDto;
import com.cwz.electronic.store.dtos.PageableResponse;
import com.cwz.electronic.store.entities.*;
import com.cwz.electronic.store.exception.BadApiRequest;
import com.cwz.electronic.store.exception.ResourceNotFoundException;
import com.cwz.electronic.store.helper.HelperForPR;
import com.cwz.electronic.store.repositories.CartRepository;
import com.cwz.electronic.store.repositories.OrderRepository;
import com.cwz.electronic.store.repositories.UserRepository;
import com.cwz.electronic.store.services.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ModelMapper mapper;

    @Override
    public OrderDto createOrder(CreateOrderRequest orderRequest) {
        String userId = orderRequest.getUserId();
        String cartId = orderRequest.getCartId();
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User does not exist."));
        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new ResourceNotFoundException("Cart not found."));
        List<CartItem> cartItems = cart.getItems();
        if (cartItems.size() <=0){
            throw new BadApiRequest("Invalid request.");
        }
        Order order = Order.builder()
                .billingName(orderRequest.getBillingName())
                .billingPhone(orderRequest.getBillingPhone())
                .billingAddress(orderRequest.getBillingAddress())
                .orderDate(new Date())
                .deliverDate(null)
                .paymentStatus(orderRequest.getPaymentStatus())
                .orderStatus(orderRequest.getOrderStatus())
                .orderId(UUID.randomUUID().toString())
                .user(user)
                .build();

        AtomicReference<Integer> orderAmount = new AtomicReference<>(0);
        List<OrderItem> orderItems = cartItems.stream().map(cartItem -> {
            OrderItem orderItem = OrderItem.builder()
                    .quantity(cartItem.getQuantity())
                    .product(cartItem.getProduct())
                    .totalPrice(cartItem.getQuantity() * cartItem.getProduct().getPrice())
                    .order(order)
                    .build();
            orderAmount.set(orderAmount.get() + orderItem.getTotalPrice());
            return orderItem;
        }).collect(Collectors.toList());
        order.setOrderItems(orderItems);
        order.setOrderAmount(orderAmount.get());

        cart.getItems().clear();
        cartRepository.save(cart);
        Order sadvedOrder = orderRepository.save(order);
        OrderDto orderDto = mapper.map(sadvedOrder, OrderDto.class);
        return orderDto;
    }

    @Override
    public OrderDto updateOrder(OrderDto orderDto, String orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("this order does not exist."));
        order.setDeliverDate(new Date());
        order.setPaymentStatus(orderDto.getPaymentStatus());
        order.setOrderStatus(orderDto.getOrderStatus());
        Order updateOrder = orderRepository.save(order);
        return mapper.map(updateOrder, OrderDto.class);
    }

    @Override
    public void removeOrder(String orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        orderRepository.delete(order);
    }

    @Override
    public List<OrderDto> getOrdersOfUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found !!"));
        List<Order> orders = orderRepository.findByUser(user);
        List<OrderDto> orderDtos = orders.stream().map(order -> mapper.map(order, OrderDto.class)).collect(Collectors.toList());
        return orderDtos;
    }

    @Override
    public PageableResponse<OrderDto> getAllOrders(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Order> page = orderRepository.findAll(pageable);
        return HelperForPR.getPageableResponse(page, OrderDto.class);
    }
}
