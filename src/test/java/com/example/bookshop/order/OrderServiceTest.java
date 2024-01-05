package com.example.bookshop.order;

import com.example.bookshop.inventory.BookDto;
import com.example.bookshop.inventory.InventoryService;
import com.example.bookshop.order.exception.NoOrdersByUserIdFoundException;
import com.example.bookshop.order.exception.NotEnoughBooksInInventory;
import com.example.bookshop.order.request.GetAllOrdersByUserIdRequest;
import com.example.bookshop.order.request.BuyOrderRequest;
import com.example.bookshop.order.response.AllOrdersByUserIdResponse;
import com.example.bookshop.order.response.BuyOrderSuccessfulResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.example.bookshop.order.constants.Constants.NoOrdersByUserIdFoundExceptionMessage;
import static com.example.bookshop.order.constants.Constants.NotEnoughBooksInInventoryExceptionMessage;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private InventoryService inventoryService;

    @InjectMocks
    private OrderService underTest;

    @Test
    void shouldCreateAnBuyOrderSuccessfully() {
        BuyOrderRequest buyOrderRequest = new BuyOrderRequest(1L, "1", 2);
        BookDto bookDto = BookDto.builder()
                                 .isbn(buyOrderRequest.isbn())
                                 .price(BigDecimal.valueOf(1))
                                 .title("test")
                                 .description("testdesc")
                                 .amount(3)
                                 .build();

        BookDto updatedBookDto = BookDto.builder()
                                        .isbn(buyOrderRequest.isbn())
                                        .price(BigDecimal.valueOf(1))
                                        .title("test")
                                        .description("testdesc")
                                        .amount(1)
                                        .build();

        Order order = Order.builder()
                           .userId(buyOrderRequest.userId())
                           .isbn(buyOrderRequest.isbn())
                           .amount(buyOrderRequest.amount())
                           .created(Date.valueOf(LocalDate.now()))
                           .build();

        BuyOrderSuccessfulResponse expected = new BuyOrderSuccessfulResponse(order.isbn, order.amount, order.created);

        when(inventoryService.findByIsbn(buyOrderRequest.isbn())).thenReturn(bookDto);
        when(inventoryService.update(updatedBookDto)).thenReturn(updatedBookDto.isbn());
        when(orderRepository.save(order)).thenReturn(order);

        BuyOrderSuccessfulResponse actual = underTest.buy(buyOrderRequest);

        verify(inventoryService, times(1)).findByIsbn(buyOrderRequest.isbn());
        verify(inventoryService, times(1)).update(updatedBookDto);
        verify(orderRepository, times(1)).save(order);

        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    void shouldThrowNotEnoughBooksInInventoryExceptionWhenAmountIsHigherThenInventory() {
        BuyOrderRequest buyOrderRequest = new BuyOrderRequest(1L, "1", 12);
        BookDto bookDto = BookDto.builder()
                                 .isbn(buyOrderRequest.isbn())
                                 .price(BigDecimal.valueOf(1))
                                 .title("test")
                                 .description("testdesc")
                                 .amount(3)
                                 .build();

        String expectedMessage = NotEnoughBooksInInventoryExceptionMessage;

        when(inventoryService.findByIsbn(buyOrderRequest.isbn())).thenReturn(bookDto);

        NotEnoughBooksInInventory actual = assertThrows(NotEnoughBooksInInventory.class, () -> {
            underTest.buy(buyOrderRequest);
        });

        verify(inventoryService, times(1)).findByIsbn(buyOrderRequest.isbn());

        assertEquals(expectedMessage, actual.getMessage());
    }

    @Test
    void shouldFindOrdersByUserId() {
        GetAllOrdersByUserIdRequest getAllOrdersByUserIdRequest = new GetAllOrdersByUserIdRequest(1L);
        Order order1 = Order.builder()
                            .userId(1L)
                            .isbn("1")
                            .amount(1)
                            .created(Date.valueOf(LocalDate.now()))
                            .build();
        Order order2 = Order.builder()
                            .userId(1L)
                            .isbn("2")
                            .amount(1)
                            .created(Date.valueOf(LocalDate.now()))
                            .build();

        List<Order> orders = List.of(order1, order2);

        AllOrdersByUserIdResponse expected = new AllOrdersByUserIdResponse(orders);

        when(orderRepository.findAllByUserId(getAllOrdersByUserIdRequest.userId())).thenReturn(Optional.of(orders));

        AllOrdersByUserIdResponse actual = underTest.getAllOrdersByUserId(getAllOrdersByUserIdRequest.userId());

        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    void shouldThrowNoOrdersByUserIdFoundExceptionWhenUserIdHasNoOrders() {
        GetAllOrdersByUserIdRequest getAllOrdersByUserIdRequest = new GetAllOrdersByUserIdRequest(1L);
        String expectedMessage = NoOrdersByUserIdFoundExceptionMessage;

        when(orderRepository.findAllByUserId(getAllOrdersByUserIdRequest.userId())).thenReturn(Optional.empty());

        NoOrdersByUserIdFoundException actual = assertThrows(
                NoOrdersByUserIdFoundException.class,
                () -> {
                    underTest.getAllOrdersByUserId(getAllOrdersByUserIdRequest.userId());
                }
        );

        assertEquals(expectedMessage, actual.getMessage());
    }
}