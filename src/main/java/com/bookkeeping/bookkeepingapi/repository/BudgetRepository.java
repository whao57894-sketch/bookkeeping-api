package com.bookkeeping.bookkeepingapi.repository;

import com.bookkeeping.bookkeepingapi.entity.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {

    List<Budget> findByPhoneAndMonth(String phone, String month);

    Budget findByPhoneAndCategoryAndMonth(String phone, String category, String month);
}
