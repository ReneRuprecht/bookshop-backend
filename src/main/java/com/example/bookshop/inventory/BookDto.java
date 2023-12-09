package com.example.bookshop.inventory;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record BookDto(String isbn, String title, String description, BigDecimal price) {
}
