package com.example.bookshop.inventory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Book, Long> {

    @Query(value = "SELECT * FROM books b WHERE b.isbn=?1", nativeQuery = true)
    Optional<Book> findByIsbn(String isbn);
}
