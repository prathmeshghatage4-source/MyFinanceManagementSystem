package com.example.MyExpenseTracker.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class BudgetStatusDTO {

    private BigDecimal spent;
    private BigDecimal limit;
    private BigDecimal remaining;
    private String status;          // ON TRACK, WARNING, EXCEEDED
}