package com.chenu.expensetracker.service;

import com.chenu.expensetracker.entity.Expense;
import com.chenu.expensetracker.entity.User;
import com.chenu.expensetracker.repository.ExpenseRepository;
import com.chenu.expensetracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<?> createExpense(
            Long userId,
            Expense expense) {

        Optional<User> optionalUser =
                userRepository.findById(userId);

        if (optionalUser.isEmpty()) {
            return new ResponseEntity<>(
                    "User not found",
                    HttpStatus.NOT_FOUND
            );
        }

        if (expense.getTitle() == null ||
                expense.getTitle().trim().isEmpty()) {

            return new ResponseEntity<>(
                    "Expense title is required",
                    HttpStatus.BAD_REQUEST
            );
        }

        if (expense.getAmount() == null ||
                expense.getAmount() <= 0) {

            return new ResponseEntity<>(
                    "Expense amount must be greater than 0",
                    HttpStatus.BAD_REQUEST
            );
        }

        if (expense.getCategory() == null ||
                expense.getCategory().trim().isEmpty()) {

            return new ResponseEntity<>(
                    "Expense category is required",
                    HttpStatus.BAD_REQUEST
            );
        }

        if (expense.getDate() == null) {
            expense.setDate(LocalDate.now());
        }

        User user = optionalUser.get();

        expense.setUser(user);

        Expense savedExpense =
                expenseRepository.save(expense);

        user.getExpenses().add(savedExpense);

        Double remainingMoney =
                user.getRemainingMoney() - expense.getAmount();

        user.setRemainingMoney(remainingMoney);

        userRepository.save(user);

        return new ResponseEntity<>(
                savedExpense,
                HttpStatus.CREATED
        );
    }

    public ResponseEntity<?> getAllExpensesOfUser(
            Long userId) {

        Optional<User> optionalUser =
                userRepository.findById(userId);

        if (optionalUser.isEmpty()) {
            return new ResponseEntity<>(
                    "User not found",
                    HttpStatus.NOT_FOUND
            );
        }

        return new ResponseEntity<>(
                optionalUser.get().getExpenses(),
                HttpStatus.OK
        );
    }

    public ResponseEntity<?> getExpenseById(
            Long userId,
            Long expenseId) {

        Optional<Expense> optionalExpense =
                expenseRepository.findById(expenseId);

        if (optionalExpense.isEmpty()) {
            return new ResponseEntity<>(
                    "Expense not found",
                    HttpStatus.NOT_FOUND
            );
        }

        Expense expense = optionalExpense.get();

        if (!expense.getUser().getId().equals(userId)) {

            return new ResponseEntity<>(
                    "Expense does not belong to this user",
                    HttpStatus.BAD_REQUEST
            );
        }

        return new ResponseEntity<>(
                expense,
                HttpStatus.OK
        );
    }

    public ResponseEntity<?> updateExpense(
            Long userId,
            Long expenseId,
            Expense updatedExpense) {

        Optional<Expense> optionalExpense =
                expenseRepository.findById(expenseId);

        if (optionalExpense.isEmpty()) {
            return new ResponseEntity<>(
                    "Expense not found",
                    HttpStatus.NOT_FOUND
            );
        }

        Expense existingExpense =
                optionalExpense.get();

        if (!existingExpense.getUser()
                .getId()
                .equals(userId)) {

            return new ResponseEntity<>(
                    "Unauthorized",
                    HttpStatus.BAD_REQUEST
            );
        }

        if (updatedExpense.getTitle() == null ||
                updatedExpense.getTitle().trim().isEmpty()) {

            return new ResponseEntity<>(
                    "Expense title is required",
                    HttpStatus.BAD_REQUEST
            );
        }

        if (updatedExpense.getAmount() == null ||
                updatedExpense.getAmount() <= 0) {

            return new ResponseEntity<>(
                    "Expense amount must be greater than 0",
                    HttpStatus.BAD_REQUEST
            );
        }

        User user = existingExpense.getUser();

        Double oldAmount = existingExpense.getAmount();

        Double newAmount = updatedExpense.getAmount();

        Double difference = newAmount - oldAmount;

        user.setRemainingMoney(
                user.getRemainingMoney() - difference
        );

        existingExpense.setTitle(updatedExpense.getTitle());
        existingExpense.setMessage(updatedExpense.getMessage());
        existingExpense.setAmount(updatedExpense.getAmount());
        existingExpense.setCategory(updatedExpense.getCategory());
        existingExpense.setDate(updatedExpense.getDate());

        expenseRepository.save(existingExpense);

        userRepository.save(user);

        return new ResponseEntity<>(
                existingExpense,
                HttpStatus.OK
        );
    }

    public ResponseEntity<?> deleteExpense(
            Long userId,
            Long expenseId) {

        Optional<Expense> optionalExpense =
                expenseRepository.findById(expenseId);

        if (optionalExpense.isEmpty()) {

            return new ResponseEntity<>(
                    "Expense not found",
                    HttpStatus.NOT_FOUND
            );
        }

        Expense expense = optionalExpense.get();

        if (!expense.getUser().getId().equals(userId)) {

            return new ResponseEntity<>(
                    "Unauthorized",
                    HttpStatus.BAD_REQUEST
            );
        }

        User user = expense.getUser();

        user.setRemainingMoney(
                user.getRemainingMoney() + expense.getAmount()
        );

        userRepository.save(user);

        expenseRepository.delete(expense);

        return new ResponseEntity<>(
                "Expense deleted successfully",
                HttpStatus.OK
        );
    }

    public ResponseEntity<?> getLastMonthSummary(
            Long userId) {

        Optional<User> optionalUser =
                userRepository.findById(userId);

        if (optionalUser.isEmpty()) {

            return new ResponseEntity<>(
                    "User not found",
                    HttpStatus.NOT_FOUND
            );
        }

        LocalDate endDate = LocalDate.now();

        LocalDate startDate = endDate.minusMonths(1);

        List<Expense> expenses =
                expenseRepository.findByUserIdAndDateBetween(
                        userId,
                        startDate,
                        endDate
                );

        double totalExpense = expenses.stream()
                .mapToDouble(Expense::getAmount)
                .sum();

        User user = optionalUser.get();

        Map<String, Object> response =
                new HashMap<>();

        response.put("totalExpenseLastMonth",
                totalExpense);

        response.put("remainingMoney",
                user.getRemainingMoney());

        response.put("totalMoney",
                user.getTotalMoney());

        return new ResponseEntity<>(
                response,
                HttpStatus.OK
        );
    }

    public ResponseEntity<?> getTop10Expenses(
            Long userId) {

        Optional<User> optionalUser =
                userRepository.findById(userId);

        if (optionalUser.isEmpty()) {

            return new ResponseEntity<>(
                    "User not found",
                    HttpStatus.NOT_FOUND
            );
        }

        List<Expense> expenses =
                expenseRepository
                        .findTop10ByUserIdOrderByDateDesc(userId);

        return new ResponseEntity<>(
                expenses,
                HttpStatus.OK
        );
    }



    public ResponseEntity<?> getByCategory(String category, Long id) {

        Optional<User> optional = userRepository.findById(id);

        if (optional.isEmpty()) {
            return new ResponseEntity<>(
                    "User not found",
                    HttpStatus.NOT_FOUND
            );
        }

        List<Expense> expenses = expenseRepository
                .findByUserIdAndCategory(id, category);

        if (expenses.isEmpty()) {
            return new ResponseEntity<>(
                    "Category not found",
                    HttpStatus.NOT_FOUND
            );
        }

        return new ResponseEntity<>(
                expenses,
                HttpStatus.OK
        );
    }

}