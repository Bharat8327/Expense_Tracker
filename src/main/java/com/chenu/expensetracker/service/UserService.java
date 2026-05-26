package com.chenu.expensetracker.service;

import com.chenu.expensetracker.dto.ExpenseDTO;
import com.chenu.expensetracker.dto.UserDTO;
import com.chenu.expensetracker.entity.Expense;
import com.chenu.expensetracker.entity.User;
import com.chenu.expensetracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

    public ResponseEntity<?> createUser(User user) {

        if (user.getUsername() == null ||
                user.getUsername().trim().isEmpty()) {

            return new ResponseEntity<>(
                    "Username is required",
                    HttpStatus.BAD_REQUEST
            );
        }

        if (user.getPassword() == null ||
                user.getPassword().trim().isEmpty()) {

            return new ResponseEntity<>(
                    "Password is required",
                    HttpStatus.BAD_REQUEST
            );
        }

        if (user.getTotalMoney() == null ||
                user.getTotalMoney() < 0) {

            return new ResponseEntity<>(
                    "Total money must be positive",
                    HttpStatus.BAD_REQUEST
            );
        }

        if (userRepository
                .findByUsername(user.getUsername())
                .isPresent()) {

            return new ResponseEntity<>(
                    "Username already exists",
                    HttpStatus.CONFLICT
            );
        }

        user.setRemainingMoney(user.getTotalMoney());
        user.setPassword(passwordEncoder().encode(user.getPassword()));

        User savedUser = userRepository.save(user);

        return new ResponseEntity<>(
                toUserDTO(savedUser),
                HttpStatus.CREATED
        );
    }

    public ResponseEntity<?> getUserByName(String username) {

        if (username == null ||
                username.trim().isEmpty()) {

            return new ResponseEntity<>(
                    "Username is required",
                    HttpStatus.BAD_REQUEST
            );
        }

        Optional<User> user =
                userRepository.findByUsername(username);

        if (user.isEmpty()) {

            return new ResponseEntity<>(
                    "User not found",
                    HttpStatus.NOT_FOUND
            );
        }

        return new ResponseEntity<>(
                toUserDTO(user.get()),
                HttpStatus.OK
        );
    }

    public ResponseEntity<?> getUserById(Long userId) {

        Optional<User> optionalUser =
                userRepository.findById(userId);

        if (optionalUser.isEmpty()) {

            return new ResponseEntity<>(
                    "User not found",
                    HttpStatus.NOT_FOUND
            );
        }
        User  user = optionalUser.get();
        return new ResponseEntity<>(
                toUserDTO(user),
                HttpStatus.OK
        );
    }


    public ResponseEntity<?> updateUser(
            User user,
            Long userId) {

        Optional<User> optionalUser =
                userRepository.findById(userId);

        if (optionalUser.isEmpty()) {

            return new ResponseEntity<>(
                    "User not found",
                    HttpStatus.NOT_FOUND
            );
        }

        User existingUser = optionalUser.get();

        if (user.getUsername() != null &&
                !user.getUsername().trim().isEmpty()) {

            Optional<User> existingUsername =
                    userRepository.findByUsername(
                            user.getUsername());

            if (existingUsername.isPresent() &&
                    !existingUsername.get()
                            .getId()
                            .equals(userId)) {

                return new ResponseEntity<>(
                        "Username already exists",
                        HttpStatus.CONFLICT
                );
            }

            existingUser.setUsername(
                    user.getUsername());
        }

        if (user.getPassword() != null &&
                !user.getPassword().trim().isEmpty()) {

            existingUser.setPassword(passwordEncoder().encode(user.getPassword()));
        }

        if (user.getTotalMoney() != null) {

            if (user.getTotalMoney() < 0) {

                return new ResponseEntity<>(
                        "Total money cannot be negative",
                        HttpStatus.BAD_REQUEST
                );
            }

            Double difference =
                    user.getTotalMoney()
                            - existingUser.getTotalMoney();

            existingUser.setTotalMoney(
                    user.getTotalMoney());

            existingUser.setRemainingMoney(
                    existingUser.getRemainingMoney()
                            + difference
            );
        }

        User updatedUser =
                userRepository.save(existingUser);

        return new ResponseEntity<>(
                toUserDTO(updatedUser),
                HttpStatus.OK
        );
    }

    public ResponseEntity<?> deleteUserById(
            Long userId) {

        Optional<User> optionalUser =
                userRepository.findById(userId);

        if (optionalUser.isEmpty()) {

            return new ResponseEntity<>(
                    "User not found",
                    HttpStatus.NOT_FOUND
            );
        }

        userRepository.deleteById(userId);

        return new ResponseEntity<>(
                "User deleted successfully",
                HttpStatus.OK
        );
    }


    public UserDTO toUserDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setTotalMoney(user.getTotalMoney());
        dto.setRemainingMoney(user.getRemainingMoney());
        return dto;
    }

    public ExpenseDTO toExpenseDTO(Expense expense) {
        ExpenseDTO dto = new ExpenseDTO();
        dto.setId(expense.getId());
        dto.setTitle(expense.getTitle());
        dto.setMessage(expense.getMessage());
        dto.setAmount(expense.getAmount());
        dto.setCategory(expense.getCategory().name());
        dto.setDate(expense.getDate());
        return dto;
    }
}