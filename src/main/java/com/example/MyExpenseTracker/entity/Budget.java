package com.example.MyExpenseTracker.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Table(name = "budget")
@Entity
@Getter
@Setter
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String category;


    private BigDecimal limitAmount; // budget limit set by user

    @ManyToOne
    private User user;


}
