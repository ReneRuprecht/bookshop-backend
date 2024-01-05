package com.example.bookshop.order;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // deactivate the default behaviour
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OrderRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Autowired
    private OrderRepository underTest;

    @BeforeAll
    void setUp() {

        Date date = Date.valueOf(LocalDate.now());

        jdbcTemplate.execute("INSERT INTO orders (id,user_id,isbn,amount,created)" +
                                     " VALUES (1,1,'testisb',2,'" + date + "');");
        jdbcTemplate.execute("INSERT INTO orders (id,user_id,isbn,amount,created)" +
                                     " VALUES (2,1,'testisb',2,'" + date + "');");

    }

    @Test
    void shouldFindOrdersByUserId() {
        Long userId = 1L;

        Optional<List<Order>> orders = underTest.findAllByUserId(userId);

        assertTrue(orders.isPresent());
        assertThat(orders.get().size()).isGreaterThan(1);
    }
}