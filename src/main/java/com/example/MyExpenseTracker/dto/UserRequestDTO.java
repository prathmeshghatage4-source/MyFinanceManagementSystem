package com.example.MyExpenseTracker.dto;



import com.example.MyExpenseTracker.entity.type.Role;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequestDTO {

    @NotBlank(message = "name cannot be blank")
    private String name;

    @Email(message = "Email not valid")
    @NotBlank(message = "Please enter email")
    private String email;

    @NotBlank(message = "password is required")
    private String password;

    @NotNull(message = "Should not be null")
    private Role role;
}