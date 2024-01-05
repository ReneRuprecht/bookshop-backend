package com.example.bookshop.order.response;

import java.util.Date;

public record BuyOrderSuccessfulResponse(String isbn, int amount, Date created) {
}
