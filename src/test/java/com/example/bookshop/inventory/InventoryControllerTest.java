package com.example.bookshop.inventory;

import com.example.bookshop.inventory.exception.BookAlreadyExistsException;
import com.example.bookshop.inventory.exception.BookNotFoundByIsbnException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class InventoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private InventoryServiceImpl inventoryService;

    @Test
    void shouldReturnEmptyListWhenServiceReturnsEmptyList() throws Exception {

        String expected = "[]";

        when(inventoryService.findAll()).thenReturn(List.of());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/inventory")
                                              .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
               .andExpect(content().string(expected));

        verify(inventoryService, times(1)).findAll();
    }

    @Test
    void shouldReturnListOfBookDto() throws Exception {

        BookDto book1 = BookDto.builder()
                               .isbn("1")
                               .title("t1")
                               .description("d1")
                               .price(BigDecimal.valueOf(1))
                               .build();

        BookDto book2 = BookDto.builder()
                               .isbn("2")
                               .title("t2")
                               .description("d2")
                               .price(BigDecimal.valueOf(2))
                               .build();

        List<BookDto> books = List.of(book1, book2);

        when(inventoryService.findAll()).thenReturn(books);

        String expected = objectMapper.writeValueAsString(books);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/inventory")
                                              .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
               .andExpect(content().string(expected));

        verify(inventoryService, times(1)).findAll();
    }

    @Test
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

        when(inventoryService.findByIsbn(book.getIsbn())).thenReturn(bookDto);

        String expected = objectMapper.writeValueAsString(bookDto);

        mockMvc.perform(MockMvcRequestBuilders.get(String.format("/api/v1/inventory/%s", book.getIsbn()))
                                              .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
               .andExpect(content().string(expected));

        verify(inventoryService, times(1)).findByIsbn(book.getIsbn());
    }

    @Test
    void shouldReturn404IfBookIsNotFoundByIsbn() throws Exception {

        Book book = Book.builder()
                        .isbn("1")
                        .title("t1")
                        .description("d1")
                        .price(BigDecimal.valueOf(1))
                        .build();

        when(inventoryService.findByIsbn(book.getIsbn())).thenThrow(BookNotFoundByIsbnException.class);

        mockMvc.perform(MockMvcRequestBuilders.get(String.format("/api/v1/inventory/%s", book.getIsbn()))
                                              .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isNotFound());

        verify(inventoryService, times(1)).findByIsbn(book.getIsbn());
    }

    @Test
    void shouldReturnIsbnWhenSaveIsSuccessful() throws Exception {

        BookDto bookDtoToSave = BookDto.builder()
                                       .isbn("1")
                                       .title("t1")
                                       .description("d1")
                                       .price(BigDecimal.valueOf(1))
                                       .build();

        String content = objectMapper.writeValueAsString(bookDtoToSave);

        when(inventoryService.save(bookDtoToSave)).thenReturn(bookDtoToSave.isbn());

        mockMvc.perform(post("/api/v1/inventory").contentType(MediaType.APPLICATION_JSON)
                                                 .content(content))
               .andExpect(status().isOk())
               .andExpect(content().string("1"));

        verify(inventoryService, times(1)).save(bookDtoToSave);
    }

    @Test
    void shouldReturn400IfSavingABookAlreadyExists() throws Exception {

        BookDto bookDtoToSave = BookDto.builder()
                                       .isbn("1")
                                       .title("t1")
                                       .description("d1")
                                       .price(BigDecimal.valueOf(1))
                                       .build();

        String content = objectMapper.writeValueAsString(bookDtoToSave);

        when(inventoryService.save(bookDtoToSave)).thenThrow(BookAlreadyExistsException.class);

        mockMvc.perform(post("/api/v1/inventory").contentType(MediaType.APPLICATION_JSON)
                                                 .content(content))
               .andExpect(status().isBadRequest());

        verify(inventoryService, times(1)).save(bookDtoToSave);
    }
}