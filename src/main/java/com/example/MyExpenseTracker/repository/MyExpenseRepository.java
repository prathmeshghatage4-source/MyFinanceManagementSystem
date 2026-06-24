package com.example.MyExpenseTracker.repository;

import com.example.MyExpenseTracker.entity.MyExpense;
import com.example.MyExpenseTracker.entity.type.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MyExpenseRepository extends JpaRepository<MyExpense, Long>, JpaSpecificationExecutor<MyExpense> {

    List<MyExpense> findByCategoryIgnoreCase(String category);

    List<MyExpense> findByPaymentMethod(PaymentMethod paymentMethod);

}