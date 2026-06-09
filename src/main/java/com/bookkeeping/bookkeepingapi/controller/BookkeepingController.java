package com.bookkeeping.bookkeepingapi.controller;

import com.bookkeeping.bookkeepingapi.dto.ApiResponse;
import com.bookkeeping.bookkeepingapi.dto.BookkeepingMonthResponse;
import com.bookkeeping.bookkeepingapi.dto.BookkeepingRequest;
import com.bookkeeping.bookkeepingapi.dto.BookkeepingResponse;
import com.bookkeeping.bookkeepingapi.dto.RecognizeBillResponse;
import com.bookkeeping.bookkeepingapi.service.BookkeepingService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin
@RestController
@RequestMapping("/api/bookkeeping")
public class BookkeepingController {

    private final BookkeepingService bookkeepingService;

    public BookkeepingController(BookkeepingService bookkeepingService) {
        this.bookkeepingService = bookkeepingService;
    }

    @GetMapping
    public ApiResponse<BookkeepingMonthResponse> month(@RequestParam String phone, @RequestParam String month) {
        try {
            return ApiResponse.ok("查询成功", bookkeepingService.getMonth(phone, month));
        } catch (IllegalArgumentException ex) {
            return ApiResponse.fail(ex.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ApiResponse<BookkeepingResponse> get(@PathVariable Long id) {
        try {
            return ApiResponse.ok("查询成功", bookkeepingService.get(id));
        } catch (IllegalArgumentException ex) {
            return ApiResponse.fail(ex.getMessage());
        }
    }

    @PostMapping
    public ApiResponse<BookkeepingResponse> create(@RequestBody BookkeepingRequest request) {
        try {
            return ApiResponse.ok("保存成功", bookkeepingService.create(request));
        } catch (IllegalArgumentException ex) {
            return ApiResponse.fail(ex.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ApiResponse<BookkeepingResponse> update(@PathVariable Long id, @RequestBody BookkeepingRequest request) {
        try {
            return ApiResponse.ok("保存成功", bookkeepingService.update(id, request));
        } catch (IllegalArgumentException ex) {
            return ApiResponse.fail(ex.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        try {
            bookkeepingService.delete(id);
            return ApiResponse.ok("删除成功", null);
        } catch (IllegalArgumentException ex) {
            return ApiResponse.fail(ex.getMessage());
        }
    }

    @PostMapping("/recognize")
    public ApiResponse<RecognizeBillResponse> recognize(@RequestPart("file") MultipartFile file) {
        return ApiResponse.ok("识别完成", bookkeepingService.recognize(file));
    }
}
