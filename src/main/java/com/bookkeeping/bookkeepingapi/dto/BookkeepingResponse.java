package com.bookkeeping.bookkeepingapi.dto;

import com.bookkeeping.bookkeepingapi.entity.BookkeepingRecord;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class BookkeepingResponse {

    private Long id;
    private String phone;
    private String type;
    private String category;
    private BigDecimal amount;
    private String recordDate;
    private String remark;

    public static BookkeepingResponse from(BookkeepingRecord record) {
        return new BookkeepingResponse(
                record.getId(),
                record.getPhone(),
                record.getType(),
                record.getCategory(),
                record.getAmount(),
                record.getRecordDate().toString(),
                record.getRemark()
        );
    }
}
