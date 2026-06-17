package com.bookkeeping.bookkeepingapi.controller;

import com.bookkeeping.bookkeepingapi.dto.ApiResponse;
import com.bookkeeping.bookkeepingapi.entity.Budget;
import com.bookkeeping.bookkeepingapi.service.BudgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/budget")
@CrossOrigin
public class BudgetController {

    @Autowired
    private BudgetService budgetService;

    @GetMapping
    public ApiResponse<List<Budget>> getBudgets(@RequestParam String phone, @RequestParam String month) {
        try {
            List<Budget> budgets = budgetService.getBudgetsByMonth(phone, month);
            return ApiResponse.ok("获取成功", budgets);
        } catch (Exception e) {
            return ApiResponse.fail("获取失败：" + e.getMessage());
        }
    }

    @PostMapping
    public ApiResponse<Budget> createBudget(@RequestBody Budget budget) {
        try {
            Budget saved = budgetService.saveBudget(budget);
            return ApiResponse.ok("添加成功", saved);
        } catch (Exception e) {
            return ApiResponse.fail("添加失败：" + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ApiResponse<Budget> updateBudget(@PathVariable Long id, @RequestBody Budget budget) {
        try {
            Budget updated = budgetService.updateBudget(id, budget);
            return ApiResponse.ok("更新成功", updated);
        } catch (Exception e) {
            return ApiResponse.fail("更新失败：" + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteBudget(@PathVariable Long id) {
        try {
            budgetService.deleteBudget(id);
            return ApiResponse.ok("删除成功", null);
        } catch (Exception e) {
            return ApiResponse.fail("删除失败：" + e.getMessage());
        }
    }
}
