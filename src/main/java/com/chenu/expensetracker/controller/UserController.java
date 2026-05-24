package com.chenu.expensetracker.controller;

import com.chenu.expensetracker.entity.User;
import com.chenu.expensetracker.repository.UserRepository;
import com.chenu.expensetracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Success");
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }


    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<?> getUserByName(
            @PathVariable String username) {
        return userService.getUserByName(username);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable Long userId) {
        return  userService.getUserById(userId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(
            @RequestBody User user,
            @PathVariable Long id) {

        return userService.updateUser(user, id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(
            @PathVariable Long id) {
        return userService.deleteUserById(id);
    }

}