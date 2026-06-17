package com.bookkeeping.bookkeepingapi.controller;

import com.bookkeeping.bookkeepingapi.dto.ApiResponse;
import com.bookkeeping.bookkeepingapi.entity.Debt;
import com.bookkeeping.bookkeepingapi.service.DebtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/debt")
@CrossOrigin
public class DebtController {

    @Autowired
    private DebtService debtService;

    @GetMapping
    public ApiResponse<List<Debt>> getDebts(@RequestParam String phone) {
        try {
            List<Debt> debts = debtService.getDebtsByPhone(phone);
            return ApiResponse.ok("获取成功", debts);
        } catch (Exception e) {
            return ApiResponse.fail("获取失败：" + e.getMessage());
        }
    }

    @PostMapping
    public ApiResponse<Debt> createDebt(@RequestBody Debt debt) {
        try {
            Debt saved = debtService.saveDebt(debt);
            return ApiResponse.ok("添加成功", saved);
        } catch (Exception e) {
            return ApiResponse.fail("添加失败：" + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ApiResponse<Debt> updateDebt(@PathVariable Long id, @RequestBody Debt debt) {
        try {
            Debt updated = debtService.updateDebt(id, debt);
            return ApiResponse.ok("更新成功", updated);
        } catch (Exception e) {
            return ApiResponse.fail("更新失败：" + e.getMessage());
        }
    }

    @PostMapping("/{id}/repay")
    public ApiResponse<Debt> repayDebt(@PathVariable Long id, @RequestBody Map<String, BigDecimal> request) {
        try {
            BigDecimal amount = request.get("amount");
            Debt updated = debtService.repayDebt(id, amount);
            return ApiResponse.ok("还款成功", updated);
        } catch (Exception e) {
            return ApiResponse.fail("还款失败：" + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteDebt(@PathVariable Long id) {
        try {
            debtService.deleteDebt(id);
            return ApiResponse.ok("删除成功", null);
        } catch (Exception e) {
            return ApiResponse.fail("删除失败：" + e.getMessage());
        }
    }
}
