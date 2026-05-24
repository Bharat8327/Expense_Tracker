package com.chenu.expensetracker.repository;

import com.chenu.expensetracker.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    List<Expense> findByUserIdAndDateBetween( // get one month expense details
            Long userId,
            LocalDate startDate,
            LocalDate endDate
    );

    List<Expense> findTop10ByUserIdOrderByDateDesc(Long userId); // get latest 10 expense

    List<Expense> findByUserIdAndCategory( // get expense by category
            Long userId,
            String category
    );

}