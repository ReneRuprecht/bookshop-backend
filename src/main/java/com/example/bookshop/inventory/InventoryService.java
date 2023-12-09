package com.example.bookshop.inventory;

import java.util.List;

public interface InventoryService {
    List<BookDto> findAll();

    BookDto findByIsbn(String isbn);

    String save(BookDto bookDto);

    String deleteByIsbn(String isbn);

}
