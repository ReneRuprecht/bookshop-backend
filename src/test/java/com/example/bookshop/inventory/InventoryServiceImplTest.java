package com.example.bookshop.inventory;

import com.example.bookshop.inventory.util.MapperImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

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

    @Test
    void shouldFindABookByIsbn() {
        String isbnToSearch = "1";
        Book book1 = Book.builder()
                         .isbn(isbnToSearch)
                         .title("t1")
                         .description("d1")
                         .price(BigDecimal.valueOf(1))
                         .build();

        BookDto bookDto1 = BookDto.builder()
                                  .isbn(isbnToSearch)
                                  .title("t1")
                                  .description("d1")
                                  .price(BigDecimal.valueOf(1))
                                  .build();

        when(mapperImpl.convertBookToBookDto(book1)).thenReturn(bookDto1);
        when(inventoryRepository.findByIsbn(isbnToSearch)).thenReturn(Optional.of(book1));

        BookDto actual = underTest.findByIsbn(isbnToSearch);

        assertNotNull(actual);

        verify(mapperImpl, times(1)).convertBookToBookDto(book1);
        verify(inventoryRepository, times(1)).findByIsbn(isbnToSearch);

        assertEquals(bookDto1, actual);
    }

    @Test
    void shouldDeleteABookByIsbn() {
        String isbnToDelete = "1";

        Book book1 = Book.builder()
                         .isbn(isbnToDelete)
                         .title("t1")
                         .description("d1")
                         .price(BigDecimal.valueOf(1))
                         .build();

        when(inventoryRepository.findByIsbn(isbnToDelete)).thenReturn(Optional.ofNullable(book1));
        assertNotNull(book1);

        String actual = underTest.deleteByIsbn(isbnToDelete);

        verify(inventoryRepository, times(1)).findByIsbn(isbnToDelete);
        verify(inventoryRepository, times(1)).delete(book1);

        assertEquals(isbnToDelete, actual);
    }

    @Test
    void shouldUpdateABook() {
        String isbn = "1";
        Book book1 = Book.builder()
                         .isbn(isbn)
                         .title("t1")
                         .description("d1")
                         .price(BigDecimal.valueOf(1))
                         .build();

        BookDto bookDto1 = BookDto.builder()
                                  .isbn(isbn)
                                  .title("t2")
                                  .description("d2")
                                  .price(BigDecimal.valueOf(2))
                                  .build();

        Book expected = Book.builder()
                            .isbn(isbn)
                            .title("t2")
                            .description("d2")
                            .price(BigDecimal.valueOf(2))
                            .build();

        when(inventoryRepository.findByIsbn(isbn)).thenReturn(Optional.ofNullable(book1));

        String actual = underTest.update(bookDto1);

        verify(inventoryRepository, times(1)).save(expected);
        verify(inventoryRepository, times(1)).findByIsbn(isbn);

        assertEquals(actual, expected.getIsbn());
    }
}