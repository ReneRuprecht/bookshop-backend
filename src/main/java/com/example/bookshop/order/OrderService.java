package com.example.bookshop.order;

import com.example.bookshop.inventory.BookDto;
import com.example.bookshop.inventory.InventoryService;
import com.example.bookshop.order.exception.NoOrdersByUserIdFoundException;
import com.example.bookshop.order.exception.NotEnoughBooksInInventory;
import com.example.bookshop.order.request.BuyOrderRequest;
import com.example.bookshop.order.response.AllOrdersByUserIdResponse;
import com.example.bookshop.order.response.BuyOrderSuccessfulResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import static com.example.bookshop.order.constants.Constants.NoOrdersByUserIdFoundExceptionMessage;
import static com.example.bookshop.order.constants.Constants.NotEnoughBooksInInventoryExceptionMessage;

@Service
@AllArgsConstructor
public class OrderService {

    private final InventoryService inventoryService;
    private final OrderRepository orderRepository;

    public BuyOrderSuccessfulResponse buy(BuyOrderRequest buyOrderRequest) {
        BookDto bookDto = this.inventoryService.findByIsbn(buyOrderRequest.isbn());

        if (buyOrderRequest.amount() > bookDto.amount()) {
            throw new NotEnoughBooksInInventory(NotEnoughBooksInInventoryExceptionMessage);
        }

        BookDto updatedBookDto = BookDto.builder()
                                        .isbn(buyOrderRequest.isbn())
                                        .title(bookDto.title())
                                        .description(bookDto.description())
                                        .price(bookDto.price())
                                        .amount(bookDto.amount() - buyOrderRequest.amount())
                                        .build();

        inventoryService.update(updatedBookDto);

        Order order = Order.builder()
                           .userId(buyOrderRequest.userId())
                           .isbn(buyOrderRequest.isbn())
                           .amount(buyOrderRequest.amount())
                           .created(Date.valueOf(LocalDate.now()))
                           .build();

        orderRepository.save(order);

        return new BuyOrderSuccessfulResponse(order.isbn, order.amount, order.created);
    }

    public AllOrdersByUserIdResponse getAllOrdersByUserId(Long userId) {
        List<Order> orders = this.orderRepository.findAllByUserId(userId).orElseThrow(
                () -> new NoOrdersByUserIdFoundException(NoOrdersByUserIdFoundExceptionMessage)
        );

        return new AllOrdersByUserIdResponse(orders);
    }
}
