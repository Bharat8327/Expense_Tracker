package com.chenu.expensetracker.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // PostgreSQL auto-generates Ids like 1,2,3...
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    private String password;

    private Double totalMoney;

    private Double remainingMoney;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)// CascadeType.ALL applies all operations automatically
    // mappedBy = "user" means Expense entity owns relationship


    @JsonManagedReference
    private List<Expense> expenses = new ArrayList<>();
}