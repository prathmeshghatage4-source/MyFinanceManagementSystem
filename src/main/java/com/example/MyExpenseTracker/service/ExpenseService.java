package com.example.MyExpenseTracker.service;

import com.example.MyExpenseTracker.dto.ExpenseRequestDTO;
import com.example.MyExpenseTracker.dto.ExpenseResponseDTO;
import com.example.MyExpenseTracker.entity.MyExpense;
import com.example.MyExpenseTracker.entity.User;
import com.example.MyExpenseTracker.entity.type.PaymentMethod;
import com.example.MyExpenseTracker.kafka.BudgetAlertProducer;
import com.example.MyExpenseTracker.repository.MyExpenseRepository;
import com.example.MyExpenseTracker.repository.UserRepository;
import com.example.MyExpenseTracker.specification.MySpecification;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final MyExpenseRepository myExpenseRepository;
    private final UserRepository userRepository;
    private final BudgetAlertProducer budgetAlertProducer;

    private static final BigDecimal BUDGET_THRESHOLD = new BigDecimal("10000"); // ₹10,000

    @Transactional
    public MyExpense getExpenseById(Long id) {
        return myExpenseRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Expense for id " + id + " doesn't exist"));
    }

    // Cache all expenses for a user
    @Cacheable(value = "expenses", key = "#email + '_' + #page + '_' + #size")
    @Transactional
    public Page<MyExpense> getAllExpense(String email, int page, int size) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        Pageable pageable = PageRequest.of(page, size);
        return myExpenseRepository.findByUserId(user.getId(), pageable);
    }

    // Once user updates the expense, remove the old cache from memory
    @CacheEvict(value = "expenses", allEntries = true)
    @Transactional
    public MyExpense updateExpense(ExpenseRequestDTO dto, Long id) {
        MyExpense existingexpense = myExpenseRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Expense with id " + id + " not found"));

        existingexpense.setAmount(dto.getAmount());
        existingexpense.setDate(dto.getDate());
        existingexpense.setCategory(dto.getCategory());
        existingexpense.setTitle(dto.getTitle());
        existingexpense.setDescription(dto.getDescription());

        PaymentMethod paymentMethod = PaymentMethod.valueOf(
                dto.getPaymentMethod().trim().toUpperCase()
        );
        existingexpense.setPaymentMethod(paymentMethod);

        return myExpenseRepository.save(existingexpense);
    }

    @CacheEvict(value = "expenses", allEntries = true)
    public String DeleteExpenseById(Long id) {
        myExpenseRepository.deleteById(id);
        return "ID " + id + " has been deleted";
    }

    @Transactional
    public List<MyExpense> getExpenseByCategory(String category) {
        return myExpenseRepository.findByCategoryIgnoreCase(category);
    }

    @Transactional
    public List<MyExpense> getExpenseByPaymentMethod(PaymentMethod paymentMethod) {
        return myExpenseRepository.findByPaymentMethod(paymentMethod);
    }

    @CacheEvict(value = "expenses", allEntries = true)
    @Transactional
    public ExpenseResponseDTO addExpense(ExpenseRequestDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new NoSuchElementException("User with id " + dto.getUserId() + " not found"));

        // DTO → Entity
        MyExpense expense = new MyExpense();
        expense.setTitle(dto.getTitle());
        expense.setDescription(dto.getDescription());
        expense.setAmount(dto.getAmount());
        expense.setCategory(dto.getCategory());
        expense.setDate(dto.getDate());

        PaymentMethod paymentMethod = PaymentMethod.valueOf(
                dto.getPaymentMethod().trim().toUpperCase()
        );
        expense.setPaymentMethod(paymentMethod);
        expense.setUser(user);

        MyExpense savedExpense = myExpenseRepository.save(expense);

        // Check budget threshold
        BigDecimal totalSpending = myExpenseRepository
                .findByUserId(user.getId(), Pageable.unpaged())
                .stream()
                .map(MyExpense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (totalSpending.compareTo(BUDGET_THRESHOLD) > 0) {

            try {
                budgetAlertProducer.sendAlert(
                        "User " + user.getEmail() +
                                " has exceeded budget threshold! Total spending: ₹" + totalSpending
                );
            }
            catch (Exception e){
                System.out.println("Kafka unavailable, skipping alert: " + e.getMessage());
            }
        }

        // Entity → ResponseDTO
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
    public Page<MyExpense> filterExpense(String category, PaymentMethod paymentMethod, String title, int page,
                                         BigDecimal amount, BigDecimal minAmount, BigDecimal maxAmount,
                                         LocalDate startdate, LocalDate enddate, int size,
                                         String sortBy, String sortDir) {

        Sort sort;
        if (sortDir.equalsIgnoreCase("asc")) {
            sort = Sort.by(sortBy).ascending();
        } else if (sortDir.equalsIgnoreCase("desc")) {
            sort = Sort.by(sortBy).descending();
        } else {
            throw new IllegalArgumentException("sortDir must be either asc or desc");
        }

        Pageable pageable = PageRequest.of(page, size, sort);
        Specification<MyExpense> specification = Specification.where(null);

        if (category != null) {
            specification = specification.and(MySpecification.hasCategory(category));
        }

        if (paymentMethod != null) {
            specification = specification.and(MySpecification.hasPaymentMethod(paymentMethod));
        }

        if (title != null) {
            specification = specification.and(MySpecification.hasTitle(title));
        }

        if (amount != null) {
            specification = specification.and(MySpecification.hasAmount(amount));
        }

        if (minAmount != null) {
            specification = specification.and(MySpecification.hasMinAmount(minAmount));
        }

        if (maxAmount != null) {
            specification = specification.and(MySpecification.hasMaxAmount(maxAmount));
        }

        if (startdate != null) {
            specification = specification.and(MySpecification.hasStartDate(startdate));
        }

        if (enddate != null) {
            specification = specification.and(MySpecification.hasEndDate(enddate));
        }

        return myExpenseRepository.findAll(specification, pageable);
    }
}