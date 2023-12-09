package com.example.bookshop.inventory;

import com.example.bookshop.inventory.exception.BookAlreadyExistsException;
import com.example.bookshop.inventory.exception.BookNotFoundByIsbnException;
import com.example.bookshop.inventory.util.Mapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final Mapper mapperImpl;

    @Override
    public List<BookDto> findAll() {
        return this.inventoryRepository.findAll()
                                       .stream()
                                       .map(this.mapperImpl::convertBookToBookDto)
                                       .collect(Collectors.toList());
    }

    public BookDto findByIsbn(String isbn) {
        return this.inventoryRepository.findByIsbn(isbn)
                                       .map(this.mapperImpl::convertBookToBookDto)
                                       .orElseThrow(() -> new BookNotFoundByIsbnException(isbn));
    }

    @Override
    public String save(BookDto bookDto) {

        if (this.inventoryRepository.findByIsbn(bookDto.isbn())
                                    .isPresent()) {
            throw new BookAlreadyExistsException(bookDto.isbn());
        }

        Book book = this.mapperImpl.convertBookDtoToBook(bookDto);

        this.inventoryRepository.save(book);
        return book.getIsbn();
    }

    @Override
    public String deleteByIsbn(String isbn) {

        Book book = this.inventoryRepository.findByIsbn(isbn)
                                            .orElseThrow(() -> new BookNotFoundByIsbnException(isbn));
        this.inventoryRepository.delete(book);
        return isbn;
    }
}
