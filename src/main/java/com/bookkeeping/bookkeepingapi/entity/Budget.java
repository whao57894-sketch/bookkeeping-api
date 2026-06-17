package com.bookkeeping.bookkeepingapi.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "budget")
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false, length = 7)
    private String month; // 格式: yyyy-MM

    @Column(precision = 10, scale = 2)
    private BigDecimal currentSpent = BigDecimal.ZERO;
}
