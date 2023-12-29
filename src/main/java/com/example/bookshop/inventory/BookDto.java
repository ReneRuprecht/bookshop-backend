package com.example.bookshop.inventory;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.Objects;

@Builder
public record BookDto(String isbn, String title, String description, BigDecimal price) {

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        BookDto bookDto = (BookDto) obj;
        return Objects.equals(isbn, bookDto.isbn) &&
                Objects.equals(title, bookDto.title) &&
                Objects.equals(description, bookDto.description) &&
                price.compareTo(bookDto.price) == 0;
    }

}