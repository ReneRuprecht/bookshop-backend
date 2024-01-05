package com.example.bookshop.order.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class NotEnoughBooksInInventory extends RuntimeException {
    public NotEnoughBooksInInventory(String message) {
        super(message);
    }
}
