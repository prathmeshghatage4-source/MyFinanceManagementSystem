package com.example.MyExpenseTracker.controller;

import com.example.MyExpenseTracker.dto.LoginRequestDTO;
import com.example.MyExpenseTracker.service.AuthService;
import com.example.MyExpenseTracker.service.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;


    @PostMapping("/login")
    public String verifyCredential(@RequestBody LoginRequestDTO dto){
        return authService.verifyCredential(dto);
    }

}
