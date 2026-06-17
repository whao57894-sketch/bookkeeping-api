package com.bookkeeping.bookkeepingapi.repository;

import com.bookkeeping.bookkeepingapi.entity.RecurringBill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecurringBillRepository extends JpaRepository<RecurringBill, Long> {

    List<RecurringBill> findByPhone(String phone);

    List<RecurringBill> findByPhoneAndIsActive(String phone, Boolean isActive);
}
