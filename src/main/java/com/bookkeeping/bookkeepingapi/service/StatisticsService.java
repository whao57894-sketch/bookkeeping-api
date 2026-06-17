package com.bookkeeping.bookkeepingapi.service;

import com.bookkeeping.bookkeepingapi.entity.BookkeepingRecord;
import com.bookkeeping.bookkeepingapi.repository.BookkeepingRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StatisticsService {

    @Autowired
    private BookkeepingRecordRepository bookkeepingRecordRepository;

    /**
     * 获取收支趋势
     */
    public List<Map<String, Object>> getMonthlyTrend(String phone, int months) {
        List<Map<String, Object>> result = new ArrayList<>();

        // 获取最近N个月的数据
        for (int i = months - 1; i >= 0; i--) {
            String month = getMonthBefore(i);
            List<BookkeepingRecord> records = bookkeepingRecordRepository
                .findByPhoneAndRecordDateStartingWith(phone, month);

            BigDecimal income = records.stream()
                .filter(r -> "收入".equals(r.getType()))
                .map(BookkeepingRecord::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal expense = records.stream()
                .filter(r -> "支出".equals(r.getType()))
                .map(BookkeepingRecord::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

            Map<String, Object> monthData = new HashMap<>();
            monthData.put("month", month);
            monthData.put("income", income);
            monthData.put("expense", expense);
            result.add(monthData);
        }

        return result;
    }

    /**
     * 获取分类统计
     */
    public List<Map<String, Object>> getCategoryStatistics(String phone, String month) {
        List<BookkeepingRecord> records = bookkeepingRecordRepository
            .findByPhoneAndRecordDateStartingWith(phone, month);

        // 按类别分组统计支出
        Map<String, BigDecimal> categoryMap = records.stream()
            .filter(r -> "支出".equals(r.getType()))
            .collect(Collectors.groupingBy(
                BookkeepingRecord::getCategory,
                Collectors.reducing(BigDecimal.ZERO,
                    BookkeepingRecord::getAmount,
                    BigDecimal::add)
            ));

        List<Map<String, Object>> result = new ArrayList<>();
        for (Map.Entry<String, BigDecimal> entry : categoryMap.entrySet()) {
            Map<String, Object> item = new HashMap<>();
            item.put("category", entry.getKey());
            item.put("amount", entry.getValue());
            result.add(item);
        }

        // 按金额降序排序
        result.sort((a, b) ->
            ((BigDecimal)b.get("amount")).compareTo((BigDecimal)a.get("amount"))
        );

        return result;
    }

    /**
     * 获取年度报告
     */
    public Map<String, Object> getYearlyReport(String phone, int year) {
        String yearPrefix = String.valueOf(year);
        List<BookkeepingRecord> records = bookkeepingRecordRepository
            .findByPhoneAndRecordDateStartingWith(phone, yearPrefix);

        BigDecimal totalIncome = records.stream()
            .filter(r -> "收入".equals(r.getType()))
            .map(BookkeepingRecord::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalExpense = records.stream()
            .filter(r -> "支出".equals(r.getType()))
            .map(BookkeepingRecord::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 储蓄率
        BigDecimal savingsRate = BigDecimal.ZERO;
        if (totalIncome.compareTo(BigDecimal.ZERO) > 0) {
            savingsRate = totalIncome.subtract(totalExpense)
                .divide(totalIncome, 4, RoundingMode.HALF_UP)
                .multiply(new BigDecimal("100"));
        }

        // 最大支出类别
        Map<String, BigDecimal> categoryMap = records.stream()
            .filter(r -> "支出".equals(r.getType()))
            .collect(Collectors.groupingBy(
                BookkeepingRecord::getCategory,
                Collectors.reducing(BigDecimal.ZERO,
                    BookkeepingRecord::getAmount,
                    BigDecimal::add)
            ));

        String topCategory = "无";
        BigDecimal topAmount = BigDecimal.ZERO;
        for (Map.Entry<String, BigDecimal> entry : categoryMap.entrySet()) {
            if (entry.getValue().compareTo(topAmount) > 0) {
                topCategory = entry.getKey();
                topAmount = entry.getValue();
            }
        }

        // 月均收支
        BigDecimal avgMonthlyIncome = totalIncome.divide(new BigDecimal("12"), 2, RoundingMode.HALF_UP);
        BigDecimal avgMonthlyExpense = totalExpense.divide(new BigDecimal("12"), 2, RoundingMode.HALF_UP);

        Map<String, Object> result = new HashMap<>();
        result.put("totalIncome", totalIncome);
        result.put("totalExpense", totalExpense);
        result.put("savingsRate", savingsRate);
        result.put("recordCount", records.size());
        result.put("topExpenseCategory", topCategory);
        result.put("topExpenseAmount", topAmount);
        result.put("avgMonthlyIncome", avgMonthlyIncome);
        result.put("avgMonthlyExpense", avgMonthlyExpense);

        return result;
    }

    private String getMonthBefore(int monthsAgo) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -monthsAgo);
        return String.format("%04d-%02d",
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH) + 1);
    }
}
