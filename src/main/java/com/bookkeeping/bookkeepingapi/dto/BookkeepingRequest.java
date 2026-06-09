package com.bookkeeping.bookkeepingapi.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class BookkeepingRequest {

    private String phone;
    private String type;
    private String category;
    private BigDecimal amount;
    private String recordDate;
    private String remark;
}
