package com.example.bookshop.inventory.util;

import com.example.bookshop.inventory.Book;
import com.example.bookshop.inventory.BookDto;
import org.springframework.stereotype.Component;

@Component
public class MapperImpl implements Mapper {
    public Book convertBookDtoToBook(BookDto bookDto) {
        Book book = new Book();
        book.setIsbn(bookDto.isbn());
        book.setTitle(bookDto.title());
        book.setDescription(bookDto.description());
        book.setPrice(bookDto.price());
        book.setAmount(bookDto.amount());
        return book;
    }

    public BookDto convertBookToBookDto(Book book) {
        return BookDto.builder()
                      .isbn(book.getIsbn())
                      .title(book.getTitle())
                      .description(book.getDescription())
                      .price(book.getPrice())
                      .amount(book.getAmount())
                      .build();
    }
}
