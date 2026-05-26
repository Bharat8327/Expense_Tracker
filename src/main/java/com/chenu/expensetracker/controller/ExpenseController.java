package com.chenu.expensetracker.controller;

import com.chenu.expensetracker.dto.ExpenseDTO;
import com.chenu.expensetracker.entity.Expense;
import com.chenu.expensetracker.service.ExpenseService;
import com.chenu.expensetracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users/{userId}/expenses")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;
    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<?> createExpense(
            @PathVariable Long userId,
            @RequestBody Expense expense) {

        return expenseService.createExpense(userId, expense);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ExpenseDTO>> getAllExpensesOfUser(@PathVariable Long userId) {
            List<Expense> expenses = expenseService.getAllExpensesOfUser(userId);
            if (expenses.isEmpty()) {
                return ResponseEntity.status(404).body(Collections.emptyList());
            }
            List<ExpenseDTO> dtos = expenses.stream()
                    .map(expense -> userService.toExpenseDTO(expense))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(dtos);
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