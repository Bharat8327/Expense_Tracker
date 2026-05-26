package com.chenu.expensetracker.dto;

import com.chenu.expensetracker.entity.ExpenseCategory;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ExpenseDTO {

    private Long id;

    private String title;

    private String message;

    private Double amount;

    private String category;

    private LocalDate date;
}