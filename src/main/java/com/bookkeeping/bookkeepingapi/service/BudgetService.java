package com.bookkeeping.bookkeepingapi.service;

import com.bookkeeping.bookkeepingapi.entity.Budget;
import com.bookkeeping.bookkeepingapi.entity.BookkeepingRecord;
import com.bookkeeping.bookkeepingapi.repository.BudgetRepository;
import com.bookkeeping.bookkeepingapi.repository.BookkeepingRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class BudgetService {

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private BookkeepingRecordRepository bookkeepingRecordRepository;

    public List<Budget> getBudgetsByMonth(String phone, String month) {
        List<Budget> budgets = budgetRepository.findByPhoneAndMonth(phone, month);

        // 更新每个预算的已用金额
        for (Budget budget : budgets) {
            BigDecimal spent = calculateSpentByCategory(phone, month, budget.getCategory());
            budget.setCurrentSpent(spent);
        }

        return budgets;
    }

    public Budget saveBudget(Budget budget) {
        // 检查是否已存在同类别同月份的预算
        Budget existing = budgetRepository.findByPhoneAndCategoryAndMonth(
            budget.getPhone(), budget.getCategory(), budget.getMonth()
        );

        if (existing != null && !existing.getId().equals(budget.getId())) {
            throw new RuntimeException("该类别的预算已存在");
        }

        return budgetRepository.save(budget);
    }

    public Budget updateBudget(Long id, Budget budget) {
        Budget existing = budgetRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("预算不存在"));

        existing.setCategory(budget.getCategory());
        existing.setAmount(budget.getAmount());
        existing.setMonth(budget.getMonth());

        return budgetRepository.save(existing);
    }

    public void deleteBudget(Long id) {
        budgetRepository.deleteById(id);
    }

    private BigDecimal calculateSpentByCategory(String phone, String month, String category) {
        List<BookkeepingRecord> records = bookkeepingRecordRepository
            .findByPhoneAndRecordDateStartingWith(phone, month);

        return records.stream()
            .filter(r -> "支出".equals(r.getType()) && category.equals(r.getCategory()))
            .map(BookkeepingRecord::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
