package com.example.MyExpenseTracker;


import com.example.MyExpenseTracker.dto.ExpenseRequestDTO;
import com.example.MyExpenseTracker.entity.MyExpense;
import com.example.MyExpenseTracker.entity.User;
import com.example.MyExpenseTracker.repository.MyExpenseRepository;
import com.example.MyExpenseTracker.repository.UserRepository;
import com.example.MyExpenseTracker.service.ExpenseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

@ExtendWith(MockitoExtension.class)
public class ExpenseServiceTest {

    @Mock
    private final MyExpenseRepository myExpenseRepository;

    @Mock
    private final UserRepository userRepository;

    @InjectMocks
    private final ExpenseService expenseService;


    private User sampleUser;

    private MyExpense sampleExpense;

    private ExpenseRequestDTO requestDTO;

    public ExpenseServiceTest(MyExpenseRepository myExpenseRepository, ExpenseService expenseService, UserRepository userRepository) {
        this.myExpenseRepository = myExpenseRepository;
        this.userRepository = userRepository;
        this.expenseService = expenseService;
    }

    @BeforeEach
    void setup(){

        sampleUser = new User();
        sampleUser.setId(1L);
        sampleUser.setEmail("Rubbish@gamil.com");
        sampleUser.setName("Rubbish");

        sampleExpense = new MyExpense();
        sampleExpense.setId(100L);
        sampleExpense.setAmount(new BigDecimal("45.50"));
        sampleExpense.setCategory("Groceries");
        sampleExpense.setDate(LocalDate.now());
        sampleExpense.setUser(sampleUser);

        requestDTO = new ExpenseRequestDTO();
        requestDTO.setAmount(new BigDecimal("45.50"));
        requestDTO.setCategory("Groceries");
        requestDTO.setDate(LocalDate.now());

    }
}
