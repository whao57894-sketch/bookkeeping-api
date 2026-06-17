package com.bookkeeping.bookkeepingapi.service;

import com.bookkeeping.bookkeepingapi.entity.Debt;
import com.bookkeeping.bookkeepingapi.repository.DebtRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class DebtService {

    @Autowired
    private DebtRepository debtRepository;

    public List<Debt> getDebtsByPhone(String phone) {
        return debtRepository.findByPhone(phone);
    }

    public Debt saveDebt(Debt debt) {
        return debtRepository.save(debt);
    }

    public Debt updateDebt(Long id, Debt debt) {
        Debt existing = debtRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("债务记录不存在"));

        existing.setType(debt.getType());
        existing.setCounterparty(debt.getCounterparty());
        existing.setAmount(debt.getAmount());
        existing.setDebtDate(debt.getDebtDate());
        existing.setDueDate(debt.getDueDate());
        existing.setRemark(debt.getRemark());

        return debtRepository.save(existing);
    }

    public Debt repayDebt(Long id, BigDecimal amount) {
        Debt debt = debtRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("债务记录不存在"));

        BigDecimal newRepaidAmount = debt.getRepaidAmount().add(amount);
        debt.setRepaidAmount(newRepaidAmount);

        // 更新状态
        if (newRepaidAmount.compareTo(debt.getAmount()) >= 0) {
            debt.setStatus("PAID");
        } else if (newRepaidAmount.compareTo(BigDecimal.ZERO) > 0) {
            debt.setStatus("PARTIAL");
        }

        return debtRepository.save(debt);
    }

    public void deleteDebt(Long id) {
        debtRepository.deleteById(id);
    }
}
