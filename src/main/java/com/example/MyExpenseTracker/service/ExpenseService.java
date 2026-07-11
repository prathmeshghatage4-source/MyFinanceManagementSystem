package com.example.MyExpenseTracker.service;

import com.example.MyExpenseTracker.dto.ExpenseRequestDTO;
import com.example.MyExpenseTracker.dto.ExpenseResponseDTO;
import com.example.MyExpenseTracker.entity.MyExpense;
import com.example.MyExpenseTracker.entity.User;
import com.example.MyExpenseTracker.entity.type.PaymentMethod;
import com.example.MyExpenseTracker.repository.MyExpenseRepository;
import com.example.MyExpenseTracker.repository.UserRepository;
import com.example.MyExpenseTracker.specification.MySpecification;
import jakarta.transaction.Transactional;
import lombok.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final MyExpenseRepository myExpenseRepository;

    private final UserRepository userRepository;

    @Transactional
    public MyExpense getExpenseById(Long id){

        MyExpense expense = null;
        try {
          expense = myExpenseRepository.findById(id).orElseThrow(() -> new RuntimeException("Expense for this id doesn't exist"));

        }
        catch(RuntimeException e){
            System.out.println(e.getMessage());
        }
        return expense;

    }

    @Transactional
    public Page<MyExpense> getAllExpense(String email, int page, int size){

        User user = userRepository.findByEmail(email).orElseThrow( () -> new NoSuchElementException("User not found"));

        Pageable pageable = PageRequest.of(page,size);

        return myExpenseRepository.findByUserId(user.getId(),pageable);
    }

    @Transactional
    public MyExpense updateExpense( ExpenseRequestDTO dto, Long id){

        MyExpense existingexpense = myExpenseRepository.findById(id).orElseThrow();

        existingexpense.setAmount(dto.getAmount());
        existingexpense.setDate(dto.getDate());
        existingexpense.setCategory(dto.getCategory());
        existingexpense.setTitle(dto.getTitle());
        existingexpense.setDescription(dto.getDescription());
        PaymentMethod paymentMethod =
                PaymentMethod.valueOf(
                        dto.getPaymentMethod().trim().toUpperCase()     //This mehtod will convert payment data , UPI , CARD in
                        //upper case if it is in other cases It will ensure there is no exception for the payment method
                );

        return myExpenseRepository.save(existingexpense);
    }

    public String DeleteExpenseById(Long id){
        myExpenseRepository.deleteById(id);
        return "ID " + id + " has been deleted";
    }


    @Transactional
    public List<MyExpense> getExpenseByCategory(String category){

        return myExpenseRepository.findByCategoryIgnoreCase(category);
    }

    @Transactional
    public List<MyExpense> getExpenseByPaymentMethod(PaymentMethod paymentMethod){
        return myExpenseRepository.findByPaymentMethod(paymentMethod);
    }


    @Transactional
    public ExpenseResponseDTO addExpense(ExpenseRequestDTO dto){

        // Find user

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new NoSuchElementException("User with id " + dto.getUserId() + " not found"));

        // DTO -> Entity
        MyExpense expense = new MyExpense();

        expense.setTitle(dto.getTitle());
        expense.setDescription(dto.getDescription());
        expense.setAmount(dto.getAmount());
        expense.setCategory(dto.getCategory());
        expense.setDate(dto.getDate());
        PaymentMethod paymentMethod =
                PaymentMethod.valueOf(
                        dto.getPaymentMethod().trim().toUpperCase()     //This mehtod will convert payment data , UPI , CARD in
                        //upper case if it is in other cases It will ensure there is no exception for the payment method
                );

        expense.setPaymentMethod(paymentMethod);

        // set relationship
        expense.setUser(user);

        // save
        MyExpense savedExpense = myExpenseRepository.save(expense);

        // Entity -> ResponseDTO
        ExpenseResponseDTO response = new ExpenseResponseDTO();

        response.setId(savedExpense.getId());
        response.setTitle(savedExpense.getTitle());
        response.setDescription(savedExpense.getDescription());
        response.setAmount(savedExpense.getAmount());
        response.setCategory(savedExpense.getCategory());
        response.setDate(savedExpense.getDate());
        response.setPaymentMethod(savedExpense.getPaymentMethod());

        response.setUserId(savedExpense.getUser().getId());

        return response;
    }


    @Transactional
    public Page<MyExpense> filterExpense(String category, PaymentMethod paymentMethod, String title, int page,BigDecimal amount,
                                         BigDecimal minAmount, BigDecimal maxAmount, LocalDate startdate, LocalDate enddate,
                                         int size, String sortBy , String sortDir){


        Sort sort;

        if(sortDir.equalsIgnoreCase("asc")){
            sort = Sort.by(sortBy).ascending();
        }
        else if(sortDir.equalsIgnoreCase("desc")){
            sort = Sort.by(sortBy).descending();
        }
        else{
            throw new IllegalArgumentException(
              "sortDir must be either asc or desc"
            );
        }

        Pageable pageable = PageRequest.of(page,size,sort);

        Specification<MyExpense> specification = Specification.where(null);

        if(category!=null){

            specification = specification.and(MySpecification.hasCategory(category));
        }

        if (paymentMethod!=null){
            specification= specification.and(MySpecification.hasPaymentMethod(paymentMethod));
        }

        if(title!=null){
            specification = specification.and(MySpecification.hasTitle(title));;
        }

        if(amount!=null){
            specification = specification.and(MySpecification.hasAmount(amount));
        }

        if(minAmount!=null){
            specification = specification.and(MySpecification.hasMinAmount(minAmount));;
        }

        if(maxAmount!=null){
            specification = specification.and(MySpecification.hasMaxAmount(maxAmount));;
        }

        if(startdate!=null){
            specification = specification.and(MySpecification.hasStartDate(startdate));;
        }

        if(enddate!=null){
            specification = specification.and(MySpecification.hasEndDate(enddate));
        }



        return myExpenseRepository.findAll(specification,pageable);
    }
}
