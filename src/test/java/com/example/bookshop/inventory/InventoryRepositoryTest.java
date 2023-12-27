package com.example.bookshop.inventory;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // deactivate the default behaviour
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class InventoryRepositoryTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private InventoryRepository underTest;

    @AfterAll
    void clean() {
        jdbcTemplate.execute("DELETE from books");
    }

    @Test
    @Order(1)
    @Rollback(value = false)
    void save() {
        Book book = new Book();
        book.setIsbn("1");
        book.setTitle("test title");
        book.setDescription("test desc");
        book.setPrice(BigDecimal.valueOf(13));

        Book savedBook = underTest.save(book);

        assertEquals(book.getIsbn(), savedBook.getIsbn());
        assertEquals(book.getTitle(), savedBook.getTitle());
        assertEquals(book.getDescription(), savedBook.getDescription());
        assertEquals(book.getPrice(), savedBook.getPrice());
    }

    @Test
    @Order(2)
    void findByIsbn() {
        Book book = new Book();
        book.setIsbn("1");
        book.setTitle("test title");
        book.setDescription("test desc");
        book.setPrice(BigDecimal.valueOf(13));

        Book bookFromDb = underTest.findByIsbn("1")
                                   .orElse(null);

        assertNotNull(bookFromDb);
        assertEquals(book.getIsbn(), bookFromDb.getIsbn());
        assertEquals(book.getTitle(), bookFromDb.getTitle());
        assertEquals(book.getDescription(), bookFromDb.getDescription());
        assertThat(book.getPrice()).isEqualByComparingTo(bookFromDb.getPrice());
    }
}