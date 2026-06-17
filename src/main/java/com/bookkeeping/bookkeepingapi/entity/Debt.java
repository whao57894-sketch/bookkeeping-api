package com.bookkeeping.bookkeepingapi.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "debt")
public class Debt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String type; // 借出/借入

    @Column(nullable = false)
    private String counterparty; // 对方姓名

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(precision = 10, scale = 2)
    private BigDecimal repaidAmount = BigDecimal.ZERO;

    @Column(nullable = false)
    private LocalDate debtDate;

    private LocalDate dueDate;

    @Column(nullable = false)
    private String status = "UNPAID"; // UNPAID, PARTIAL, PAID

    private String remark;
}
