package com.example.bookshop.inventory;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping
    public ResponseEntity<List<BookDto>> index() {
        List<BookDto> bookDtos = this.inventoryService.findAll();
        return ResponseEntity.ok(bookDtos);
    }

    @GetMapping("{isbn}")
    public ResponseEntity<BookDto> findByIsbn(@PathVariable String isbn) {
        BookDto book = this.inventoryService.findByIsbn(isbn);
        return ResponseEntity.ok(book);
    }

    @PostMapping
    public ResponseEntity<String> save(@RequestBody BookDto bookDto) {
        String response = inventoryService.save(bookDto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{isbn}")
    public ResponseEntity<String> deleteByIsbn(@PathVariable String isbn) {
        String response = inventoryService.deleteByIsbn(isbn);
        return ResponseEntity.ok(response);
    }
}
