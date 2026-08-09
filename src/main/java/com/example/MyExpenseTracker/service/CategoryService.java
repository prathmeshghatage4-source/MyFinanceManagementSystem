package com.example.MyExpenseTracker.service;
import com.example.MyExpenseTracker.entity.User;
import com.example.MyExpenseTracker.repository.MyExpenseRepository;
import com.example.MyExpenseTracker.repository.UserRepository;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CategoryService {

    private final MyExpenseRepository myExpenseRepository;
    private final UserRepository userRepository;

    public CategoryService(MyExpenseRepository myExpenseRepository, UserRepository userRepository) {
        this.myExpenseRepository = myExpenseRepository;
        this.userRepository = userRepository;
    }

    public Map<String, BigDecimal> getAnalytics(String email){

        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("email not found"));

        List<Object[]>  results = myExpenseRepository.sumByCategory(user.getId());

        Map<String, BigDecimal> analytics = new LinkedHashMap<>();

        for(Object[] row : results){
            String category = (String) row[0];
            BigDecimal total = (BigDecimal) row[1];
            analytics.put(category,total);
        }

        return analytics;
    }

    //Return highest spending category
    public String getTopCategory(String email){

        Map<String,BigDecimal> analytics = getAnalytics(email);

        return analytics.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("No Expense found");
    }
}
