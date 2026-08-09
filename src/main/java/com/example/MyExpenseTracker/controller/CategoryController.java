package com.example.MyExpenseTracker.controller;

import com.example.MyExpenseTracker.service.CategoryService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/analytics")
    public Map<String, BigDecimal> getAnalytics(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        return categoryService.getAnalytics(email);
    }


    @GetMapping("/top")
    public String getTopCategory(){

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        return categoryService.getTopCategory(email);

    }
}



