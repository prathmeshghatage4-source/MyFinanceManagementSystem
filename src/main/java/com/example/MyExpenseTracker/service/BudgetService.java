package com.example.MyExpenseTracker.service;

import com.example.MyExpenseTracker.dto.BudgetRequestDTO;
import com.example.MyExpenseTracker.dto.BudgetStatusDTO;
import com.example.MyExpenseTracker.entity.Budget;
import com.example.MyExpenseTracker.entity.User;
import com.example.MyExpenseTracker.repository.BudgetRepository;
import com.example.MyExpenseTracker.repository.MyExpenseRepository;
import com.example.MyExpenseTracker.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final MyExpenseRepository expenseRepository;
    private final UserRepository userRepository;

    public BudgetService(BudgetRepository budgetRepository,
                         MyExpenseRepository expenseRepository,
                         UserRepository userRepository) {
        this.budgetRepository = budgetRepository;
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
    }

    // Set or update budget limit for a category
    public Budget setBudget(String email, BudgetRequestDTO dto) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        // Check if budget already exists for this category
        Budget budget = budgetRepository
                .findByUserIdAndCategory(user.getId(), dto.getCategory())
                .orElse(new Budget()); // create new if not exists

        budget.setCategory(dto.getCategory());
        budget.setLimitAmount(dto.getLimitAmount());
        budget.setUser(user);

        return budgetRepository.save(budget);
    }

    // Get budget status for all categories
    public Map<String, BudgetStatusDTO> getBudgetStatus(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        // Get all budgets set by this user
        List<Budget> budgets = budgetRepository.findByUserId(user.getId());

        // Get spending per category
        List<Object[]> spending = expenseRepository.sumByCategory(user.getId());

        // Build spending map: category → amount spent
        Map<String, BigDecimal> spendingMap = new LinkedHashMap<>();
        for (Object[] row : spending) {
            spendingMap.put((String) row[0], (BigDecimal) row[1]);
        }

        // Build status map
        Map<String, BudgetStatusDTO> statusMap = new LinkedHashMap<>();

        for (Budget budget : budgets) {
            String category = budget.getCategory();
            BigDecimal limit = budget.getLimitAmount();
            BigDecimal spent = spendingMap.getOrDefault(category, BigDecimal.ZERO);
            BigDecimal remaining = limit.subtract(spent);

            // Determine status
            String status;
            double pct = spent.doubleValue() / limit.doubleValue() * 100;

            if (pct >= 100) {
                status = "EXCEEDED";
            } else if (pct >= 80) {
                status = "WARNING";
            } else {
                status = "ON TRACK";
            }

            statusMap.put(category, new BudgetStatusDTO(spent, limit, remaining, status));
        }

        return statusMap;
    }
}