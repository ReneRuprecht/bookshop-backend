package com.example.bookshop.order.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NoOrdersByUserIdFoundException extends RuntimeException{
    public NoOrdersByUserIdFoundException(String message){
        super(message);
    }
}
