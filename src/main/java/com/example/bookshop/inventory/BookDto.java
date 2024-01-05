package com.example.bookshop.inventory;

import lombok.Builder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

@Builder
public record BookDto(String isbn, String title, String description, BigDecimal price, int amount) {
    @Override
    public BigDecimal price() {
        return price.setScale(2, RoundingMode.DOWN);
    }

    @Override
    public String toString() {
        return "BookDto{" +
                "isbn='" + isbn() + '\'' +
                ", title='" + title() + '\'' +
                ", description='" + description() + '\'' +
                ", price=" + price() +
                ", amount=" + amount() +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        BookDto bookDto = (BookDto) obj;
        return Objects.equals(isbn(), bookDto.isbn()) &&
                Objects.equals(title(), bookDto.title()) &&
                Objects.equals(description(), bookDto.description()) &&
                Objects.equals(amount(), bookDto.amount()) &&
                Objects.equals(price(), bookDto.price());
    }

}