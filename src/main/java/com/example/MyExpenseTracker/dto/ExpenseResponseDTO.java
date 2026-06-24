package com.example.MyExpenseTracker.dto;

import com.example.MyExpenseTracker.entity.type.PaymentMethod;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class ExpenseResponseDTO {

    private Long id;

    private String title;

    private String description;

    private BigDecimal amount;

    private String category;

    private LocalDate date;

    private PaymentMethod paymentMethod;

    private Long userId;
}