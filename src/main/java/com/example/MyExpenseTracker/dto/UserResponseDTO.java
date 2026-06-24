package com.example.MyExpenseTracker.dto;

import com.example.MyExpenseTracker.entity.type.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDTO {

    private Long id;

    private String name;

    private String email;

    private Role role;
}