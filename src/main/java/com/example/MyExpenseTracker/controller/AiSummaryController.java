package com.example.MyExpenseTracker.controller;

import com.example.MyExpenseTracker.entity.MyExpense;
import com.example.MyExpenseTracker.entity.User;
import com.example.MyExpenseTracker.repository.MyExpenseRepository;
import com.example.MyExpenseTracker.repository.UserRepository;
import com.example.MyExpenseTracker.service.GeminiService;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/ai")
public class AiSummaryController {

    private final GeminiService geminiService;
    private final MyExpenseRepository expenseRepository;
    private final UserRepository userRepository;

    public AiSummaryController(GeminiService geminiService,
                               MyExpenseRepository expenseRepository,
                               UserRepository userRepository) {
        this.geminiService = geminiService;
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/summary")
    public String getSpendingSummary() throws Exception {

        // Get logged in user
        String email = SecurityContextHolder.getContext()
                .getAuthentication().getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        // Get user's expenses (last 20)
        List<MyExpense> expenses = expenseRepository
                .findByUserId(user.getId(), PageRequest.of(0, 20))
                .getContent();

        if (expenses.isEmpty()) {
            return "No expenses found. Add some expenses to get an AI spending summary.";
        }

        return geminiService.generateSpendingSummary(expenses);
    }
}