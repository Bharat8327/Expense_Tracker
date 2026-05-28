package com.chenu.expensetracker.service;

import com.chenu.expensetracker.entity.Expense;
import com.chenu.expensetracker.entity.ExpenseCategory;
import com.chenu.expensetracker.entity.User;
import com.chenu.expensetracker.exception.BadRequestException;
import com.chenu.expensetracker.exception.ForbiddenException;
import com.chenu.expensetracker.exception.ResourceNotFoundException;
import com.chenu.expensetracker.repository.ExpenseRepository;
import com.chenu.expensetracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@Component
public class ExpenseService {

    @Autowired
    private final ExpenseRepository expenseRepository;

    @Autowired
    private final UserRepository userRepository;

    public ExpenseService(
            UserRepository userRepository,
            ExpenseRepository expenseRepository
    ) {
        this.userRepository = userRepository;
        this.expenseRepository = expenseRepository;
    }

    public ResponseEntity<?> createExpense(
            Long userId,
            Expense expense) {
        if (expense.getCategory() == null) {
            throw new BadRequestException("Category is required. Allowed values: " + ExpenseCategory.valuesList());
            }

        Optional<User> optionalUser =
                userRepository.findById(userId);

        if (optionalUser.isEmpty()) {
            throw new ResourceNotFoundException("User not found");
        }

        if (expense.getTitle() == null ||
                expense.getTitle().trim().isEmpty()) {
            throw new BadRequestException(
                    "Expense title is required"
            );
        }

        if (expense.getAmount() == null ||
                expense.getAmount() <= 0) {

            throw new BadRequestException(
                    "Expense amount must be greater than 0"
            );
        }

        if (expense.getCategory() == null) {

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

    public List<Expense> getAllExpensesOfUser(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            return optionalUser.get().getExpenses();
        } else {
            return Collections.emptyList();
        }
    }

    public ResponseEntity<?> getExpenseById(
            Long userId,
            Long expenseId) {

        Optional<Expense> optionalExpense =
                expenseRepository.findById(expenseId);

        if (optionalExpense.isEmpty()) {
            throw new ResourceNotFoundException(
                    "Expense not found"
            );
        }

        Expense expense = optionalExpense.get();

        if (!expense.getUser().getId().equals(userId)) {
            throw new ForbiddenException(
                    "Access denied"
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
            throw new ResourceNotFoundException(
                    "Expense not found"
            );
        }

        Expense existingExpense =
                optionalExpense.get();

        if (!existingExpense.getUser()
                .getId()
                .equals(userId)) {
            throw new BadRequestException(
                    "Unauthorized"
            );
        }

        if (updatedExpense.getTitle() == null ||
                updatedExpense.getTitle().trim().isEmpty()) {
            throw new BadRequestException("Expense title is required");
        }

        if (updatedExpense.getAmount() == null ||
                updatedExpense.getAmount() <= 0) {
            throw new BadRequestException("Expense amount must be greater than 0");

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
            throw new ResourceNotFoundException("Expense not found");
        }

        Expense expense = optionalExpense.get();

        if (!expense.getUser().getId().equals(userId)) {
            throw new BadRequestException("Unauthorized");
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
            throw new ResourceNotFoundException("User not found");
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
            throw new ResourceNotFoundException("User not found");
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
            throw new ResourceNotFoundException(
                    "User not found"
            );
        }
        ExpenseCategory categoryEnum;
        try {
            categoryEnum =  ExpenseCategory.valueOf(
                            category.toUpperCase()
                    );

        } catch (Exception e) {

            throw new BadRequestException(
                    "Invalid category. Allowed values: "
                            + ExpenseCategory.valuesList()
            );
        }
        List<Expense> expenses =
                expenseRepository
                        .findByUserIdAndCategory(id, categoryEnum);

        if (expenses.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No expenses found for category: " + category
            );
        }

        return new ResponseEntity<>(
                expenses,
                HttpStatus.OK
        );

    }

}