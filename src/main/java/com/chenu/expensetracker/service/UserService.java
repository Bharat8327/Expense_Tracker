package com.chenu.expensetracker.service;

import com.chenu.expensetracker.entity.User;
import com.chenu.expensetracker.exception.BadRequestException;
import com.chenu.expensetracker.exception.ConflictException;
import com.chenu.expensetracker.exception.ResourceNotFoundException;
import com.chenu.expensetracker.repository.UserRepository;
import com.chenu.expensetracker.util.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseEntity<?> createUser(User user) {

        if (user.getUsername() == null ||
                user.getUsername().trim().isEmpty()) {
            throw new BadRequestException("Username cannot be empty");
        }

        if (user.getPassword() != null &&
                !user.getPassword().trim().isEmpty()) {
            if (user.getPassword().length() < 6) {
                throw new BadRequestException(
                        "Password must be at least 6 characters"
                );
            }
        }

        if (user.getTotalMoney() == null ||
                user.getTotalMoney() < 0) {
            throw new BadRequestException("Total money must be positive");
        }

        if (userRepository
                .findByUsername(user.getUsername())
                .isPresent()) {
             throw new ConflictException("Username already exists");
        }

        user.setRemainingMoney(user.getTotalMoney());
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User savedUser = userRepository.save(user);

        return new ResponseEntity<>(
                UserMapper.toUserDTO(savedUser),
                HttpStatus.CREATED
        );
    }

    public ResponseEntity<?> getUserByName(String username) {

        if (username == null ||
                username.trim().isEmpty()) {
            throw new BadRequestException("Username is required");
        }

        Optional<User> user =
                userRepository.findByUsername(username);

        if (user.isEmpty()) {
            throw new ResourceNotFoundException("User not found");
        }

        return new ResponseEntity<>(
                UserMapper.toUserDTO(user.get()),
                HttpStatus.OK
        );
    }

    public ResponseEntity<?> getUserById(Long userId) {

        Optional<User> optionalUser =
                userRepository.findById(userId);

        if (optionalUser.isEmpty()) {
            throw new ResourceNotFoundException("User not found");
        }
        User  user = optionalUser.get();
        return new ResponseEntity<>(
                UserMapper.toUserDTO(user),
                HttpStatus.OK
        );
    }


    public ResponseEntity<?> updateUser(
            User user,
            Long userId) {

        Optional<User> optionalUser =
                userRepository.findById(userId);

        if (optionalUser.isEmpty()) {
        throw new ResourceNotFoundException("User not found");
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

                throw new ConflictException("Username already exists");
            }

            existingUser.setUsername(
                    user.getUsername());
        }

        if (user.getPassword() != null &&
                !user.getPassword().trim().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        if (user.getTotalMoney() != null) {
            if (user.getTotalMoney() < 0) {
                throw new BadRequestException("Total money cannot be negative");
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
                UserMapper.toUserDTO(updatedUser),
                HttpStatus.OK
        );
    }

    public ResponseEntity<?> deleteUserById(
            Long userId) {

        Optional<User> optionalUser =
                userRepository.findById(userId);

        if (optionalUser.isEmpty()) {
            throw new ResourceNotFoundException("User not found");
        }

        userRepository.deleteById(userId);

        return new ResponseEntity<>(
                "User deleted successfully",
                HttpStatus.OK
        );
    }





}