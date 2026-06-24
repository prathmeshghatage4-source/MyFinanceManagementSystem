package com.example.MyExpenseTracker.specification;

import com.example.MyExpenseTracker.entity.MyExpense;
import com.example.MyExpenseTracker.entity.type.PaymentMethod;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

public class MySpecification {

    public static Specification<MyExpense> hasCategory(String category){

        return(root,query,cb) ->
                 cb.equal(cb.lower(root.get("category")),category.toLowerCase());
    }

    public static Specification<MyExpense> hasPaymentMethod(PaymentMethod paymentMethod){

        return(root,query,cb) -> cb.equal(root.get("paymentMethod"),paymentMethod);
    }

    public static Specification<MyExpense> hasTitle(String title){

        return(root,query, cb)-> cb.equal(cb.lower(root.get("title")), title.toLowerCase());
    }

    public static Specification<MyExpense> hasAmount(BigDecimal amount){
        return(root,query,cb) -> cb.equal(root.get("amount"),amount);
    }

    public static Specification<MyExpense> hasMinAmount(BigDecimal amount){
        return(root,query,cb) -> cb.greaterThanOrEqualTo(root.get("amount"), amount);
    }

    public static Specification<MyExpense> hasMaxAmount(BigDecimal amount){
        return(root,query,cb) -> cb.lessThanOrEqualTo(root.get("amount"),amount);
    }

    public static Specification<MyExpense> hasStartDate(LocalDate date){
        return(root,query,cb) ->  cb.greaterThanOrEqualTo(root.get("date"), date);
    }

    public static Specification<MyExpense> hasEndDate(LocalDate date){
        return((root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("date"),date));
    }
}
