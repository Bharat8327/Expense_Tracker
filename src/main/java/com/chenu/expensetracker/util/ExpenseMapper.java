package com.chenu.expensetracker.util;

import com.chenu.expensetracker.dto.ExpenseDTO;
import com.chenu.expensetracker.entity.Expense;

public class ExpenseMapper {
    public static ExpenseDTO toExpenseDTO(Expense expense) {
        ExpenseDTO dto = new ExpenseDTO();
        dto.setId(expense.getId());
        dto.setTitle(expense.getTitle());
        dto.setMessage(expense.getMessage());
        dto.setAmount(expense.getAmount());
        dto.setCategory( expense.getCategory() != null
                ? expense.getCategory().name()
                : null);
        dto.setDate(expense.getDate());
        return dto;
    }
}
