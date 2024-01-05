package com.example.bookshop.order.request;

public record BuyOrderRequest(Long userId, String isbn, int amount) {
}
