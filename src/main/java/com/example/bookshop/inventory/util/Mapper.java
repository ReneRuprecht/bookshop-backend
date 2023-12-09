package com.example.bookshop.inventory.util;

import com.example.bookshop.inventory.Book;
import com.example.bookshop.inventory.BookDto;

public interface Mapper {
    BookDto convertBookToBookDto(Book book);

    Book convertBookDtoToBook(BookDto bookDto);
}
