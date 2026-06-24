package com.example.MyExpenseTracker.dto;

import com.example.MyExpenseTracker.entity.type.PaymentMethod;
import jakarta.persistence.Enumerated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.NotBlank;
import org.aspectj.bridge.IMessage;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class ExpenseRequestDTO {

    @NotBlank(message = "Please enter title")
    private String title;

    private String description;

    @NotNull(message = "Please enter amount")
    @Positive(message = "Amount must be greater than 0")
    private BigDecimal amount;

    private String category;

    @CreationTimestamp
    private LocalDate date;

    @NotBlank
    private String paymentMethod;

    // which user owns this expense
    @NotNull(message = "user id can't be null")
    private Long userId;
}