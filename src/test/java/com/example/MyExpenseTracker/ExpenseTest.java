package com.example.MyExpenseTracker;


import com.example.MyExpenseTracker.controller.ExpenseController;
import com.example.MyExpenseTracker.entity.MyExpense;
import com.example.MyExpenseTracker.repository.MyExpenseRepository;
import com.example.MyExpenseTracker.service.ExpenseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class ExpenseTest {

    @Autowired
    private  MyExpenseRepository mr;

    @Autowired
    private  ExpenseService es;

    @Autowired
    private ExpenseController expenseController;

    @Test
    public void transactionalGetExpenseById(){
        MyExpense myExpense = es.getExpenseById(2L);

        System.out.println("Here are all the expenses details by Id  " + myExpense);
    }

//    @Test
//    public void transactionalGetAllExpense(){
//           List<MyExpense> myExpenseList = es.getAllExpense(0,2);
//
//           System.out.println(myExpenseList);
//    }



}
