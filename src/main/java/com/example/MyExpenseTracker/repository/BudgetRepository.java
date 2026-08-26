package com.example.MyExpenseTracker.repository;

import com.example.MyExpenseTracker.entity.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public interface BudgetRepository extends JpaRepository<Budget,Long> {

    // Find budget for a specific category and user
    Optional<Budget> findByUserIdAndCategory(Long userId, String category);

    // Find all budgets for a user
    List<Budget> findByUserId(Long userId);

}
