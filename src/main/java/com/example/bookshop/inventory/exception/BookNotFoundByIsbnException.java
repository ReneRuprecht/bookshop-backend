package com.example.bookshop.inventory.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class BookNotFoundByIsbnException extends RuntimeException {
    public BookNotFoundByIsbnException(String isbn) {
        super(String.format("Book with isbn: %s does not exist", isbn));
    }
}
