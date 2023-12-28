package com.example.bookshop.inventory.util;

import com.example.bookshop.inventory.Book;
import com.example.bookshop.inventory.BookDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MapperImplTest {

    private MapperImpl underTest;

    @BeforeAll
    void setup() {
        underTest = new MapperImpl();
    }

    @Test
    void convertBookDtoToBook() {
        BookDto bookDto = BookDto.builder()
                                 .isbn("1")
                                 .title("t1")
                                 .description("d1")
                                 .price(BigDecimal.valueOf(1))
                                 .build();

        Book expectedBook = Book.builder()
                                .isbn("1")
                                .title("t1")
                                .description("d1")
                                .price(BigDecimal.valueOf(1))
                                .build();

        Book actualBook = underTest.convertBookDtoToBook(bookDto);

        assertEquals(expectedBook, actualBook);
    }

    @Test
    void convertBookToBookDto() {
        Book book = Book.builder()
                        .isbn("1")
                        .title("t1")
                        .description("d1")
                        .price(BigDecimal.valueOf(1))
                        .build();

        BookDto expectedBookDto = BookDto.builder()
                                         .isbn("1")
                                         .title("t1")
                                         .description("d1")
                                         .price(BigDecimal.valueOf(1))
                                         .build();

        BookDto actualBookDto = underTest.convertBookToBookDto(book);

        assertEquals(expectedBookDto, actualBookDto);
    }
}