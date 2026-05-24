package com.chenu.expensetracker.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "expenses")
@Data
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    private String title;

    private String message;

    private Double amount;

    private String category;

    private LocalDate date;

    @ManyToOne



    @JoinColumn(name = "user_id")// Foreign key column in PostgreSQL

    @JsonBackReference
    private User user;
}