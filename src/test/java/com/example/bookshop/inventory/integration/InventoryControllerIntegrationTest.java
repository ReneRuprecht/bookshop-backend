package com.example.bookshop.inventory.integration;

import com.example.bookshop.inventory.Book;
import com.example.bookshop.inventory.BookDto;
import com.example.bookshop.inventory.InventoryServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class InventoryControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private InventoryServiceImpl inventoryService;

    @BeforeAll
    void setup() {
        jdbcTemplate.execute("DELETE from books");
    }

    @AfterAll
    void clean() {
        jdbcTemplate.execute("DELETE from books");
    }

    @Test
    @Order(1)
    void shouldReturnEmptyListWhenServiceReturnsEmptyList() throws Exception {

        String expected = "[]";

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/inventory")
                                              .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
               .andExpect(content().string(expected));
    }

    @Test
    @Order(6)
    void shouldReturnListOfBookDto() throws Exception {

        BookDto book1 = BookDto.builder()
                               .isbn("1")
                               .title("t1")
                               .description("d1")
                               .price(BigDecimal.valueOf(1))
                               .build();

        List<BookDto> books = List.of(book1);

        String expected = objectMapper.writeValueAsString(books);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/inventory")
                                              .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
               .andExpect(content().string(expected));
    }

    @Test
    @Order(5)
    void shouldReturnBookDtoWhenAIsbnIsRequested() throws Exception {

        Book book = Book.builder()
                        .isbn("1")
                        .title("t1")
                        .description("d1")
                        .price(BigDecimal.valueOf(1))
                        .build();

        BookDto bookDto = BookDto.builder()
                                 .isbn("1")
                                 .title("t1")
                                 .description("d1")
                                 .price(BigDecimal.valueOf(1))
                                 .build();

        String expected = objectMapper.writeValueAsString(bookDto);

        mockMvc.perform(MockMvcRequestBuilders.get(String.format("/api/v1/inventory/%s", book.getIsbn()))
                                              .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
               .andExpect(content().string(expected));
    }

    @Test
    @Order(2)
    void shouldReturn404IfBookIsNotFoundByIsbn() throws Exception {

        Book book = Book.builder()
                        .isbn("1")
                        .title("t1")
                        .description("d1")
                        .price(BigDecimal.valueOf(1))
                        .build();

        mockMvc.perform(MockMvcRequestBuilders.get(String.format("/api/v1/inventory/%s", book.getIsbn()))
                                              .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isNotFound());
    }

    @Test
    @Order(3)
    void shouldReturnIsbnWhenSaveIsSuccessful() throws Exception {

        BookDto bookDtoToSave = BookDto.builder()
                                       .isbn("1")
                                       .title("t1")
                                       .description("d1")
                                       .price(BigDecimal.valueOf(1))
                                       .build();

        String content = objectMapper.writeValueAsString(bookDtoToSave);

        mockMvc.perform(post("/api/v1/inventory").contentType(MediaType.APPLICATION_JSON)
                                                 .content(content))
               .andExpect(status().isOk())
               .andExpect(content().string("1"));
    }

    @Test
    @Order(4)
    void shouldReturn400IfSavingABookAlreadyExists() throws Exception {

        BookDto bookDtoToSave = BookDto.builder()
                                       .isbn("1")
                                       .title("t1")
                                       .description("d1")
                                       .price(BigDecimal.valueOf(1))
                                       .build();

        String content = objectMapper.writeValueAsString(bookDtoToSave);

        mockMvc.perform(post("/api/v1/inventory").contentType(MediaType.APPLICATION_JSON)
                                                 .content(content))
               .andExpect(status().isBadRequest());
    }
}