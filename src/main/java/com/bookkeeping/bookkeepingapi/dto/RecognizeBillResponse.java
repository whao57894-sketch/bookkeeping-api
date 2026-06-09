package com.bookkeeping.bookkeepingapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class RecognizeBillResponse {

    private String type;
    private String category;
    private BigDecimal amount;
    private String recordDate;
    private String remark;

    public static RecognizeBillResponse empty(String fileName) {
        return new RecognizeBillResponse("支出", "", BigDecimal.ZERO, LocalDate.now().toString(), "图片：" + fileName);
    }
}
