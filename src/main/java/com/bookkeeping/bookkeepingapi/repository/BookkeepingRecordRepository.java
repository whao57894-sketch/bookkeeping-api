package com.bookkeeping.bookkeepingapi.repository;

import com.bookkeeping.bookkeepingapi.entity.BookkeepingRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface BookkeepingRecordRepository extends JpaRepository<BookkeepingRecord, Long> {

    List<BookkeepingRecord> findByPhoneAndRecordDateBetweenOrderByRecordDateDescIdDesc(String phone, LocalDate start, LocalDate end);

    List<BookkeepingRecord> findByPhoneAndRecordDateStartingWith(String phone, String datePrefix);

    long countByPhone(String phone);
}
