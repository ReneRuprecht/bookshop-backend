package com.example.bookshop.order.response;

import com.example.bookshop.order.Order;

import java.util.List;

public record AllOrdersByUserIdResponse(List<Order> orders) {
}
