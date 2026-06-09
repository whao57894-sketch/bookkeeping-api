package com.bookkeeping.bookkeepingapi.service;

import com.bookkeeping.bookkeepingapi.dto.BookkeepingMonthResponse;
import com.bookkeeping.bookkeepingapi.dto.BookkeepingRequest;
import com.bookkeeping.bookkeepingapi.dto.BookkeepingResponse;
import com.bookkeeping.bookkeepingapi.dto.RecognizeBillResponse;
import com.bookkeeping.bookkeepingapi.entity.BookkeepingRecord;
import com.bookkeeping.bookkeepingapi.repository.BookkeepingRecordRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookkeepingService {

    private final BookkeepingRecordRepository recordRepository;

    public BookkeepingService(BookkeepingRecordRepository recordRepository) {
        this.recordRepository = recordRepository;
    }

    public BookkeepingMonthResponse getMonth(String phone, String month) {
        YearMonth yearMonth = YearMonth.parse(month);
        LocalDate start = yearMonth.atDay(1);
        LocalDate end = yearMonth.atEndOfMonth();
        List<BookkeepingRecord> records = recordRepository.findByPhoneAndRecordDateBetweenOrderByRecordDateDescIdDesc(phone, start, end);

        BigDecimal income = BigDecimal.ZERO;
        BigDecimal expense = BigDecimal.ZERO;
        for (BookkeepingRecord record : records) {
            if ("收入".equals(record.getType())) {
                income = income.add(record.getAmount());
            } else {
                expense = expense.add(record.getAmount());
            }
        }

        return new BookkeepingMonthResponse(
                income,
                expense,
                records.stream().map(BookkeepingResponse::from).collect(Collectors.toList())
        );
    }

    public BookkeepingResponse get(Long id) {
        return BookkeepingResponse.from(find(id));
    }

    public BookkeepingResponse create(BookkeepingRequest request) {
        BookkeepingRecord record = new BookkeepingRecord();
        fill(record, request);
        record.setCreatedAt(LocalDateTime.now());
        return BookkeepingResponse.from(recordRepository.save(record));
    }

    public BookkeepingResponse update(Long id, BookkeepingRequest request) {
        BookkeepingRecord record = find(id);
        fill(record, request);
        return BookkeepingResponse.from(recordRepository.save(record));
    }

    public void delete(Long id) {
        recordRepository.delete(find(id));
    }

    public long countByPhone(String phone) {
        return recordRepository.countByPhone(phone);
    }

    public RecognizeBillResponse recognize(MultipartFile file) {
        String fileName = file == null ? "" : file.getOriginalFilename();
        return RecognizeBillResponse.empty(StringUtils.defaultString(fileName, ""));
    }

    private BookkeepingRecord find(Long id) {
        return recordRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("记账记录不存在"));
    }

    private void fill(BookkeepingRecord record, BookkeepingRequest request) {
        if (StringUtils.isAnyBlank(request.getPhone(), request.getType(), request.getCategory(), request.getRecordDate()) || request.getAmount() == null) {
            throw new IllegalArgumentException("请完整填写记账信息");
        }
        if (!"收入".equals(request.getType()) && !"支出".equals(request.getType())) {
            throw new IllegalArgumentException("请选择收入或支出");
        }
        if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("金额必须大于 0");
        }

        record.setPhone(request.getPhone());
        record.setType(request.getType());
        record.setCategory(request.getCategory());
        record.setAmount(request.getAmount());
        record.setRecordDate(LocalDate.parse(request.getRecordDate()));
        record.setRemark(request.getRemark());
    }
}
