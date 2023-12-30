package com.example.bookshop.inventory.integration;

import com.example.bookshop.inventory.Book;
import com.example.bookshop.inventory.BookDto;
import com.example.bookshop.inventory.InventoryServiceImpl;
import com.example.bookshop.inventory.exception.BookAlreadyExistsException;
import com.example.bookshop.inventory.exception.BookNotFoundByIsbnException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class InventoryServiceImplIntegrationTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private InventoryServiceImpl underTest;

    @BeforeAll
    void setup() {
        jdbcTemplate.execute("DELETE from books");
        jdbcTemplate.execute("insert into books (id,isbn,title,description,price) values ('1','1','t1','d1',1)");
        jdbcTemplate.execute("insert into books (id,isbn,title,description,price) values ('2','2','t2','d2',2)");
    }

    @AfterAll
    void clean() {
        jdbcTemplate.execute("DELETE from books");
    }

    @Test
    @Order(1)
    void shouldFindAllBooks() {

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

        List<BookDto> actualListOfBookDto = underTest.findAll();

        assertEquals(expectedListOfBookDto.size(), actualListOfBookDto.size());

        assertNotNull(actualListOfBookDto);
        assertEquals(2, actualListOfBookDto.size());
        BookDto actualBookDto1 = actualListOfBookDto.get(0);
        BookDto actualBookDto2 = actualListOfBookDto.get(1);
        assertThat(bookDto1).isEqualTo(actualBookDto1);
        assertThat(bookDto2).isEqualTo(actualBookDto2);

    }

    @Test
    @Order(2)
    void shouldFindABookByIsbn() {
        String isbnToSearch = "1";

        BookDto expectedBookDto = BookDto.builder()
                                         .isbn(isbnToSearch)
                                         .title("t1")
                                         .description("d1")
                                         .price(BigDecimal.valueOf(1))
                                         .build();

        BookDto actualBookDto = underTest.findByIsbn(isbnToSearch);

        assertNotNull(actualBookDto);

        assertEquals(expectedBookDto, actualBookDto);
    }

    @Test
    @Order(6)
    void shouldThrowBookNotFoundException() {
        String isbnToSearch = "1";
        String expectedMessage = String.format("Book with isbn: %s does not exist", isbnToSearch);

        BookNotFoundByIsbnException actualException = assertThrows(BookNotFoundByIsbnException.class, () -> {
            underTest.findByIsbn(isbnToSearch);
        });

        assertEquals(expectedMessage, actualException.getMessage());
    }


    @Test
    @Order(3)
    void shouldThrowBookAlreadyExistsException() {
        String isbnToSearch = "1";
        String expectedMessage = String.format("Book with isbn: %s does already exist", isbnToSearch);

        BookDto bookDtoToSave = BookDto.builder()
                                       .isbn(isbnToSearch)
                                       .title("t2")
                                       .description("d2")
                                       .price(BigDecimal.valueOf(2))
                                       .build();

        BookAlreadyExistsException actualException = assertThrows(BookAlreadyExistsException.class, () -> {
            underTest.save(bookDtoToSave);
        });

        assertEquals(expectedMessage, actualException.getMessage());
    }

    @Test
    @Order(4)
    void shouldReturnIsbnWhenDeletingABookByIsbn() {
        String expectedIsbnToDelete = "1";

        Book book1 = Book.builder()
                         .isbn(expectedIsbnToDelete)
                         .title("t1")
                         .description("d1")
                         .price(BigDecimal.valueOf(1))
                         .build();

        assertNotNull(book1);

        String actualIsbn = underTest.deleteByIsbn(expectedIsbnToDelete);

        assertEquals(expectedIsbnToDelete, actualIsbn);
    }

    @Test
    @Order(5)
    void shouldReturnIsbnWhenUpdatingABook() {
        String isbn = "2";

        BookDto bookDto1 = BookDto.builder()
                                  .isbn(isbn)
                                  .title("t99")
                                  .description("d99")
                                  .price(BigDecimal.valueOf(99))
                                  .build();

        Book expectedBook = Book.builder()
                                .isbn(isbn)
                                .title("t99")
                                .description("d99")
                                .price(BigDecimal.valueOf(99))
                                .build();

        String actualIsbn = underTest.update(bookDto1);

        assertEquals(actualIsbn, expectedBook.getIsbn());
    }
}
