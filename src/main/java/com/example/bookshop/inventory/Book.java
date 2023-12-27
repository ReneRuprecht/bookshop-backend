package com.example.bookshop.inventory;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@Entity
@Table(name = "Books")
@Builder
@AllArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String isbn;
    private String title;
    private String description;
    private BigDecimal price;
}
