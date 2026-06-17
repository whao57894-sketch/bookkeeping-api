package com.bookkeeping.bookkeepingapi.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "recurring_bill")
public class RecurringBill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String type; // 收入/支出

    @Column(nullable = false)
    private String category;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false)
    private String frequency; // DAILY, WEEKLY, MONTHLY, YEARLY

    @Column(nullable = false)
    private LocalDate startDate;

    private LocalDate endDate;

    private Integer dayOfMonth; // 每月第几天

    private Integer dayOfWeek; // 每周第几天

    @Column(nullable = false)
    private Boolean isActive = true;

    private String remark;
}
