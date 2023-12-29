package com.example.bookshop.inventory;

import com.example.bookshop.inventory.exception.BookAlreadyExistsException;
import com.example.bookshop.inventory.exception.BookNotFoundByIsbnException;
import com.example.bookshop.inventory.util.MapperImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
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

        List<BookDto> expectedListOfBookDto = List.of(bookDto1, bookDto2);

        when(inventoryRepository.findAll()).thenReturn(books);

        when(mapperImpl.convertBookToBookDto(book1)).thenReturn(bookDto1);
        when(mapperImpl.convertBookToBookDto(book2)).thenReturn(bookDto2);

        List<BookDto> actualListOfBookDto = underTest.findAll();

        verify(inventoryRepository, times(1)).findAll();

        assertEquals(expectedListOfBookDto.size(), actualListOfBookDto.size());

        assertNotNull(actualListOfBookDto);
        assertEquals(2, actualListOfBookDto.size());
        assertEquals(bookDto1, actualListOfBookDto.get(0));
        assertEquals(bookDto2, actualListOfBookDto.get(1));

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

        BookDto expectedBookDto = BookDto.builder()
                                         .isbn(isbnToSearch)
                                         .title("t1")
                                         .description("d1")
                                         .price(BigDecimal.valueOf(1))
                                         .build();

        when(mapperImpl.convertBookToBookDto(book1)).thenReturn(expectedBookDto);
        when(inventoryRepository.findByIsbn(isbnToSearch)).thenReturn(Optional.of(book1));

        BookDto actualBookDto = underTest.findByIsbn(isbnToSearch);

        assertNotNull(actualBookDto);

        verify(mapperImpl, times(1)).convertBookToBookDto(book1);
        verify(inventoryRepository, times(1)).findByIsbn(isbnToSearch);

        assertEquals(expectedBookDto, actualBookDto);
    }

    @Test
    void shouldThrowBookNotFoundException() {
        String isbnToSearch = "1";
        String expectedMessage = String.format("Book with isbn: %s does not exist", isbnToSearch);

        when(inventoryRepository.findByIsbn(isbnToSearch)).thenReturn(Optional.empty());

        BookNotFoundByIsbnException actualException = assertThrows(BookNotFoundByIsbnException.class, () -> {
            underTest.findByIsbn(isbnToSearch);
        });

        verify(inventoryRepository, times(1)).findByIsbn(isbnToSearch);

        assertEquals(expectedMessage, actualException.getMessage());
    }


    @Test
    void shouldThrowBookAlreadyExistsException() {
        String isbnToSearch = "1";
        String expectedMessage = String.format("Book with isbn: %s does already exist", isbnToSearch);
        Book book1 = Book.builder()
                         .isbn(isbnToSearch)
                         .title("t1")
                         .description("d1")
                         .price(BigDecimal.valueOf(1))
                         .build();

        BookDto bookDtoToSave = BookDto.builder()
                                       .isbn(isbnToSearch)
                                       .title("t2")
                                       .description("d2")
                                       .price(BigDecimal.valueOf(2))
                                       .build();

        when(inventoryRepository.findByIsbn(isbnToSearch)).thenReturn(Optional.of(book1));

        BookAlreadyExistsException actualException = assertThrows(BookAlreadyExistsException.class, () -> {
            underTest.save(bookDtoToSave);
        });

        verify(inventoryRepository, times(1)).findByIsbn(isbnToSearch);

        assertEquals(expectedMessage, actualException.getMessage());
    }

    @Test
    void shouldDeleteABookByIsbn() {
        String expectedIsbnToDelete = "1";

        Book book1 = Book.builder()
                         .isbn(expectedIsbnToDelete)
                         .title("t1")
                         .description("d1")
                         .price(BigDecimal.valueOf(1))
                         .build();

        when(inventoryRepository.findByIsbn(expectedIsbnToDelete)).thenReturn(Optional.ofNullable(book1));
        assertNotNull(book1);

        String actualIsbn = underTest.deleteByIsbn(expectedIsbnToDelete);

        verify(inventoryRepository, times(1)).findByIsbn(expectedIsbnToDelete);
        verify(inventoryRepository, times(1)).delete(book1);

        assertEquals(expectedIsbnToDelete, actualIsbn);
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

        Book expectedBook = Book.builder()
                                .isbn(isbn)
                                .title("t2")
                                .description("d2")
                                .price(BigDecimal.valueOf(2))
                                .build();

        when(inventoryRepository.findByIsbn(isbn)).thenReturn(Optional.ofNullable(book1));

        String actualIsbn = underTest.update(bookDto1);

        verify(inventoryRepository, times(1)).save(expectedBook);
        verify(inventoryRepository, times(1)).findByIsbn(isbn);

        assertEquals(actualIsbn, expectedBook.getIsbn());
    }
}