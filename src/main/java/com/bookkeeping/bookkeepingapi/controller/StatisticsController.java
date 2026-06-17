package com.bookkeeping.bookkeepingapi.controller;

import com.bookkeeping.bookkeepingapi.dto.ApiResponse;
import com.bookkeeping.bookkeepingapi.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/statistics")
@CrossOrigin
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    @GetMapping("/trend")
    public ApiResponse<List<Map<String, Object>>> getMonthlyTrend(
            @RequestParam String phone,
            @RequestParam int months) {
        try {
            List<Map<String, Object>> data = statisticsService.getMonthlyTrend(phone, months);
            return ApiResponse.ok("获取成功", data);
        } catch (Exception e) {
            return ApiResponse.fail("获取失败：" + e.getMessage());
        }
    }

    @GetMapping("/category")
    public ApiResponse<List<Map<String, Object>>> getCategoryStatistics(
            @RequestParam String phone,
            @RequestParam String month) {
        try {
            List<Map<String, Object>> data = statisticsService.getCategoryStatistics(phone, month);
            return ApiResponse.ok("获取成功", data);
        } catch (Exception e) {
            return ApiResponse.fail("获取失败：" + e.getMessage());
        }
    }

    @GetMapping("/yearly")
    public ApiResponse<Map<String, Object>> getYearlyReport(
            @RequestParam String phone,
            @RequestParam int year) {
        try {
            Map<String, Object> data = statisticsService.getYearlyReport(phone, year);
            return ApiResponse.ok("获取成功", data);
        } catch (Exception e) {
            return ApiResponse.fail("获取失败：" + e.getMessage());
        }
    }
}
