package com.example.MyExpenseTracker.controller;

import com.example.MyExpenseTracker.dto.BudgetRequestDTO;
import com.example.MyExpenseTracker.dto.BudgetStatusDTO;
import com.example.MyExpenseTracker.entity.Budget;
import com.example.MyExpenseTracker.service.BudgetService;
import jakarta.validation.Valid;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/budget")
public class BudgetController {

    private final BudgetService budgetService;

    public BudgetController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    // POST /budget/set → set budget for a category
    @PostMapping("/set")
    public Budget setBudget(@Valid @RequestBody BudgetRequestDTO dto) {
        String email = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        return budgetService.setBudget(email, dto);
    }

    // GET /budget/status → check all categories vs limits
    @GetMapping("/status")
    public Map<String, BudgetStatusDTO> getBudgetStatus() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        return budgetService.getBudgetStatus(email);
    }
}