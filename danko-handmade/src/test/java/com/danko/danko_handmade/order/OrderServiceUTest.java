package com.danko.danko_handmade.order;

import com.danko.danko_handmade.email.service.EmailService;
import com.danko.danko_handmade.exception.OrderNotFoundException;
import com.danko.danko_handmade.order.model.Order;
import com.danko.danko_handmade.order.model.OrderStatus;
import com.danko.danko_handmade.order.repository.OrderRepository;
import com.danko.danko_handmade.order.service.OrderService;
import com.danko.danko_handmade.product.model.Product;
import com.danko.danko_handmade.user.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceUTest {

    @Mock
    private EmailService emailService;
    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;


    @Test
    void givenNoSuchOrderExists_whenGetOrderById_thenExceptionIsThrown() {
        UUID orderId = UUID.randomUUID();
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());
        assertThrows(OrderNotFoundException.class, () -> orderService.getOrderById(orderId));
        verify(orderRepository, times(1)).findById(orderId);
    }

    @Test
    void givenOrderExists_whenGetOrderById_thenCorrectOrderIsReturned() {
        UUID orderId = UUID.randomUUID();
        Order order = Order.builder()
                .userId(UUID.randomUUID())
                .totalPaid(BigDecimal.valueOf(100))
                .orderStatus(OrderStatus.IN_PROGRESS)
                .id(orderId)
                .build();
        when(orderRepository.findById(orderId)).thenReturn(Optional.ofNullable(order));
        Order result = orderService.getOrderById(orderId);

        assertEquals(orderId, result.getId());
        assertEquals(BigDecimal.valueOf(100), result.getTotalPaid());
        assertEquals(OrderStatus.IN_PROGRESS, result.getOrderStatus());
        assert order != null;
        assertEquals(order.getUserId(), result.getUserId());
        verify(orderRepository, times(1)).findById(orderId);
    }

    @Test
    void givenCartContent_whenCreateOrder_thenTotalPriceIsCalculatedCorrectly() {

        Product product1 = Product.builder()
                .id(UUID.randomUUID())
                .listingTitle("Product 1")
                .price(BigDecimal.valueOf(10))
                .build();
        Product product2 = Product.builder()
                .id(UUID.randomUUID())
                .listingTitle("Product 2")
                .price(BigDecimal.valueOf(20))
                .build();

        Map<Product, Integer> cartContent = new HashMap<>();
        cartContent.put(product1, 2);
        cartContent.put(product2, 3);

        User user = User.builder()
                .id(UUID.randomUUID())
                .username("User 1")
                .email("test@example.com")
                .build();

        when(orderRepository.save(any(Order.class))).thenReturn(null);
        orderService.createOrder(user, cartContent);

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).save(orderCaptor.capture());

        Order order = orderCaptor.getValue();

        BigDecimal expectedTotalPrice = BigDecimal.valueOf(80);
        assertEquals(expectedTotalPrice, order.getTotalPaid());
    }

    @Test
    void givenCartContent_whenCreateOrder_thenOrderIsCreatedAndSaved() {

        Product product = Product.builder()
                .id(UUID.randomUUID())
                .listingTitle("Product 1")
                .price(BigDecimal.valueOf(10))
                .build();

        Map<Product, Integer> cartContent = new HashMap<>();
        cartContent.put(product, 2);

        User user = User.builder()
                .id(UUID.randomUUID())
                .username("User 1")
                .email("test@example.com")
                .build();

        when(orderRepository.save(any(Order.class))).thenReturn(null);

        orderService.createOrder(user, cartContent);

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).save(orderCaptor.capture());

        Order order = orderCaptor.getValue();

        assertEquals(user.getId(), order.getUserId());
        verify(orderRepository, times(1)).save(orderCaptor.capture());
    }

    @Test
    void givenUserId_whenGetAllOrdersByUserIdNewestFirst_thenOrdersAreReturned() {

        UUID userId = UUID.randomUUID();

        Order order1 = Order.builder()
                .orderNumber("123")
                .userId(userId)
                .orderDate(LocalDateTime.now().minusDays(1))
                .build();
        Order order2 = Order.builder()
                .orderNumber("124")
                .userId(userId)
                .orderDate(LocalDateTime.now())
                .build();

        List<Order> mockOrders = Arrays.asList(order2, order1);

        when(orderRepository.findAllByUserIdOrderByOrderDateDesc(userId)).thenReturn(mockOrders);

        List<Order> result = orderService.getAllOrdersByUserIdNewestFirst(userId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(order2.getOrderNumber(), result.get(0).getOrderNumber());
        assertEquals(order1.getOrderNumber(), result.get(1).getOrderNumber());
        verify(orderRepository, times(1)).findAllByUserIdOrderByOrderDateDesc(userId);
    }

    @Test
    void givenOrdersExist_whenGetAllOrdersByOrderedOnDesc_thenOrdersAreReturned() {

        Order order1 = Order.builder()
                .orderNumber("123")
                .userId(UUID.randomUUID())
                .orderDate(LocalDateTime.now().minusDays(1))
                .build();
        Order order2 = Order.builder()
                .orderNumber("124")
                .userId(UUID.randomUUID())
                .orderDate(LocalDateTime.now())
                .build();

        List<Order> mockOrders = Arrays.asList(order2, order1);

        when(orderRepository.findAllByOrderByOrderDateDesc()).thenReturn(mockOrders);

        List<Order> result = orderService.getAllOrdersByOrderedOnDesc();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(order2.getOrderNumber(), result.get(0).getOrderNumber());
        assertEquals(order1.getOrderNumber(), result.get(1).getOrderNumber());
        verify(orderRepository, times(1)).findAllByOrderByOrderDateDesc();
    }

    @Test
    void givenLastOrderExists_whenGenerateOrderNumber_thenNewNumberIsGenerated() {
        Order lastOrder = Order.builder()
                .orderNumber("2025-123")
                .orderDate(LocalDateTime.now().minusDays(1))
                .build();

        when(orderRepository.findTopByOrderByOrderDateDesc()).thenReturn(lastOrder);

        String generatedOrderNumber = orderService.generateOrderNumber();

        String expectedOrderNumber = LocalDateTime.now().getYear() + "-124";
        assertEquals(expectedOrderNumber, generatedOrderNumber);
        verify(orderRepository, times(1)).findTopByOrderByOrderDateDesc();
    }

}

