package com.example.bookshop.inventory;

import com.example.bookshop.inventory.util.MapperImpl;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryServiceImplTest {

    @Mock
    private InventoryRepository inventoryRepository;

    @Mock
    private MapperImpl mapperImpl;

    @InjectMocks
    private InventoryServiceImpl underTest;

    @Test
    void shouldFindAllBooks() {

        Book book1 = Book.builder()
                         .isbn("1")
                         .title("t1")
                         .description("d1")
                         .price(BigDecimal.valueOf(1))
                         .build();
        Book book2 = Book.builder()
                         .isbn("2")
                         .title("t2")
                         .description("d2")
                         .price(BigDecimal.valueOf(2))
                         .build();

        List<Book> books = List.of(book1, book2);

        BookDto bookDto1 = BookDto.builder()
                                  .isbn("1")
                                  .title("t1")
                                  .description("d1")
                                  .price(BigDecimal.valueOf(1))
                                  .build();

        BookDto bookDto2 = BookDto.builder()
                                  .isbn("2")
                                  .title("t2")
                                  .description("d2")
                                  .price(BigDecimal.valueOf(2))
                                  .build();

        List<BookDto> expected = List.of(bookDto1, bookDto2);

        when(inventoryRepository.findAll()).thenReturn(books);

        when(mapperImpl.convertBookToBookDto(book1)).thenReturn(bookDto1);
        when(mapperImpl.convertBookToBookDto(book2)).thenReturn(bookDto2);

        List<BookDto> actual = underTest.findAll();

        verify(inventoryRepository, times(1)).findAll();

        assertEquals(expected.size(), actual.size());

        assertNotNull(actual);
        assertEquals(2, actual.size());
        assertEquals(bookDto1, actual.get(0));
        assertEquals(bookDto2, actual.get(1));

        verify(inventoryRepository, times(1)).findAll();
        verify(mapperImpl, times(2)).convertBookToBookDto(any());
    }

    @Disabled
    @Test
    void shouldFindABookByIsbn() {
    }

    @Disabled
    @Test
    void shouldDeleteABookByIsbn() {
    }

    @Disabled
    @Test
    void shouldUpdateABook() {
    }
}