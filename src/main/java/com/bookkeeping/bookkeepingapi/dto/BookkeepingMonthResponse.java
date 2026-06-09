package com.bookkeeping.bookkeepingapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@AllArgsConstructor
public class BookkeepingMonthResponse {

    private BigDecimal incomeTotal;
    private BigDecimal expenseTotal;
    private List<BookkeepingResponse> records;
}
