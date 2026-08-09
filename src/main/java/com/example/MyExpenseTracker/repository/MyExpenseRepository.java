package com.example.MyExpenseTracker.repository;

import com.example.MyExpenseTracker.entity.MyExpense;
import com.example.MyExpenseTracker.entity.User;
import com.example.MyExpenseTracker.entity.type.PaymentMethod;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface MyExpenseRepository extends JpaRepository<MyExpense, Long>, JpaSpecificationExecutor<MyExpense> {

    List<MyExpense> findByCategoryIgnoreCase(String category);

    List<MyExpense> findByPaymentMethod(PaymentMethod paymentMethod);

    Page<MyExpense> findByUserId(Long user, Pageable pageable);

    @Query("SELECT SUM(e.amount) FROM MyExpense e WHERE e.user.id = :userId")
    BigDecimal sumAmountByUserId(@Param("userId") Long userId);

    @Query("SELECT e.category, SUM(e.amount) FROM MyExpense e WHERE e.user.id = :userId GROUP BY e.category ORDER BY SUM(e.amount) DESC")
    List<Object[]> sumByCategory(@Param("userId") Long userId);
}