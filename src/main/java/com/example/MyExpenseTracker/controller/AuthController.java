package com.example.MyExpenseTracker.controller;

import com.example.MyExpenseTracker.dto.LoginRequestDTO;
import com.example.MyExpenseTracker.dto.UserRequestDTO;
import com.example.MyExpenseTracker.dto.UserResponseDTO;
import com.example.MyExpenseTracker.entity.User;
import com.example.MyExpenseTracker.service.AuthService;
import com.example.MyExpenseTracker.service.JWTService;
import com.example.MyExpenseTracker.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/login")
    public String verifyCredential(@RequestBody LoginRequestDTO dto){
        return authService.verifyCredential(dto);
    }

    @PostMapping("/register")
    public UserResponseDTO addUser(@Valid @RequestBody UserRequestDTO dto){
        return userService.addUser(dto);
    }
}
