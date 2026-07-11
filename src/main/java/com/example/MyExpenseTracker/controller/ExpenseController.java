package com.example.MyExpenseTracker.controller;


import com.example.MyExpenseTracker.dto.ExpenseRequestDTO;
import com.example.MyExpenseTracker.dto.ExpenseResponseDTO;
import com.example.MyExpenseTracker.entity.MyExpense;
import com.example.MyExpenseTracker.entity.type.PaymentMethod;
import com.example.MyExpenseTracker.repository.MyExpenseRepository;
import com.example.MyExpenseTracker.service.ExpenseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.aspectj.bridge.IMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/expense")

@Tag(name = "Expense APIs", description = "Operations related to expenses")

public class ExpenseController {

    private final MyExpenseRepository mr;

    private final ExpenseService expenseService;

    public ExpenseController(MyExpenseRepository myExpenseRepository, ExpenseService expenseService){ this.mr = myExpenseRepository;
        this.expenseService = expenseService;

    }

    @GetMapping("")
    public Page<MyExpense> getAllExpense(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size){

        String email = SecurityContextHolder.getContext().getAuthentication().getName();


        return expenseService.getAllExpense(email,page,size);
    }

    @GetMapping("/{id}")
    @Operation(summary = "We will get expense details by id")
    public MyExpense getExpenseById(@PathVariable Long id){

           MyExpense me = expenseService.getExpenseById(id);
           return me;
    }

    @GetMapping("/category/{category}")
    public List<MyExpense> getExpenseByCategory(@PathVariable String category){
        return expenseService.getExpenseByCategory(category);

    }

    @GetMapping("/payment")
    public List<MyExpense> getExpenseByPaymentMethod(@RequestParam PaymentMethod paymentMethod){
        return expenseService.getExpenseByPaymentMethod(paymentMethod);
    }

    @PostMapping
    @Operation(summary = "Adding of new Expense")
    public ExpenseResponseDTO addExpense(@Valid @RequestBody ExpenseRequestDTO myExpense){ return expenseService.addExpense(myExpense);}

    @PutMapping("/{id}")
    @Operation(summary = "Updation of existing  Expense")
    public MyExpense updateExpense(@RequestBody ExpenseRequestDTO updatedmyExpense,@PathVariable Long id){

        return expenseService.updateExpense(updatedmyExpense,id);
    }

    @DeleteMapping("/{id}")
    public String DeleteExpenseById(@PathVariable  Long id){
       return expenseService.DeleteExpenseById(id);
    }


    @GetMapping("/filter")
    public Page<MyExpense> filterExpense(  @RequestParam(required = false) String category,

                                                    @RequestParam(required = false)
                                                    String title,

                                           @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                               @RequestParam(required = false)
                                               LocalDate startdate,

                                           @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                               @RequestParam(required = false)
                                               LocalDate enddate,

                                           @RequestParam(required = false)
                                            BigDecimal amount,

                                           @RequestParam(required = false)
                                               BigDecimal minAmount,

                                           @RequestParam(required = false)
                                               BigDecimal maxAmount,

                                           @RequestParam(required = false)
                                                    PaymentMethod paymentMethod,

                                           @RequestParam(defaultValue = "date")
                                               String sortBy,

                                           @RequestParam(defaultValue = "desc")
                                              String sortDir,

                                                    @RequestParam(defaultValue = "0")
                                                        int page,

                                                    @RequestParam(defaultValue = "10")
                                                        int size){


        return expenseService.filterExpense(category,paymentMethod,title,page,amount,minAmount,maxAmount,startdate,enddate,size, sortBy,sortDir);
    }
}
