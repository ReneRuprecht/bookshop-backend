package com.example.bookshop.order;

import com.example.bookshop.order.request.BuyOrderRequest;
import com.example.bookshop.order.request.GetAllOrdersByUserIdRequest;
import com.example.bookshop.order.response.AllOrdersByUserIdResponse;
import com.example.bookshop.order.response.BuyOrderSuccessfulResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/order")
@AllArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<BuyOrderSuccessfulResponse> buy(@RequestBody BuyOrderRequest buyOrderRequest) {
        return new ResponseEntity<>(this.orderService.buy(buyOrderRequest), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<AllOrdersByUserIdResponse> getAllOrdersFromUserId(
            @RequestBody GetAllOrdersByUserIdRequest getAllOrdersByUserIdRequest
    ) {
        return new ResponseEntity<>(
                this.orderService.getAllOrdersByUserId(getAllOrdersByUserIdRequest.userId()),
                HttpStatus.OK
        );
    }

}
