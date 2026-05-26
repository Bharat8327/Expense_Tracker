package com.chenu.expensetracker.entity;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum ExpenseCategory {

    FOOD,
    TRANSPORT,
    SHOPPING,
    HEALTH,
    ENTERTAINMENT,
    BILLS,
    EDUCATION,
    TRAVEL,
    OTHER;

    public static String valuesList() {
        return Arrays.stream(ExpenseCategory.values())
                .map(Enum::name)
                .collect(Collectors.joining(", "));
    }
}