package com.chenu.expensetracker.controller;

import com.chenu.expensetracker.entity.Expense;
import com.chenu.expensetracker.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users/{userId}/expenses")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @PostMapping
    public ResponseEntity<?> createExpense(
            @PathVariable Long userId,
            @RequestBody Expense expense) {

        return expenseService.createExpense(userId, expense);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllExpensesOfUser(
            @PathVariable Long userId) {

        return expenseService.getAllExpensesOfUser(userId);
    }

    @GetMapping("/{expenseId}")
    public ResponseEntity<?> getExpenseById(
            @PathVariable Long userId,
            @PathVariable Long expenseId) {

        return expenseService.getExpenseById(userId, expenseId);
    }

    @PutMapping("/{expenseId}")
    public ResponseEntity<?> updateExpense(
            @PathVariable Long userId,
            @PathVariable Long expenseId,
            @RequestBody Expense expense) {

        return expenseService.updateExpense(
                userId,
                expenseId,
                expense);
    }

    @DeleteMapping("/{expenseId}")
    public ResponseEntity<?> deleteExpense(
            @PathVariable Long userId,
            @PathVariable Long expenseId) {

        return expenseService.deleteExpense(userId, expenseId);
    }


    @GetMapping
    public ResponseEntity<?> getExpensesTop10(@PathVariable Long userId) {
        return expenseService.getTop10Expenses(userId);
    }

    @GetMapping("/month")
    public ResponseEntity<?> getExpensesOneMonth(
            @PathVariable Long userId) {
        return expenseService.getLastMonthSummary(userId);
    }

    @GetMapping("/c/{category}")
    public ResponseEntity<?> getExpensesByCategory(@PathVariable String category,@PathVariable Long userId) {
        return expenseService.getByCategory(category,userId);
    }

}