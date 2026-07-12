package com.example.MyExpenseTracker.controller;

import com.example.MyExpenseTracker.dto.AuthResponseDto;
import com.example.MyExpenseTracker.dto.LoginRequestDTO;
import com.example.MyExpenseTracker.dto.UserRequestDTO;
import com.example.MyExpenseTracker.dto.UserResponseDTO;
import com.example.MyExpenseTracker.entity.RefreshToken;
import com.example.MyExpenseTracker.entity.User;
import com.example.MyExpenseTracker.service.AuthService;
import com.example.MyExpenseTracker.service.JWTService;
import com.example.MyExpenseTracker.service.RefreshTokenService;
import com.example.MyExpenseTracker.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;
    private final JWTService jwtService;

    @PostMapping("/login")
    public AuthResponseDto verifyCredential(@RequestBody LoginRequestDTO dto){
        return authService.verifyCredential(dto);
    }

    @PostMapping("/register")
    public UserResponseDTO addUser(@Valid @RequestBody UserRequestDTO dto){
        return userService.addUser(dto);
    }

    @PostMapping("/refresh")
    public AuthResponseDto refresh(@RequestBody Map<String, String> request) {

        String requestToken = request.get("refreshToken");

        // Validate the refresh token
        RefreshToken refreshToken = refreshTokenService.validateRefreshToken(requestToken);

        // Generate new access token
        String newAccessToken = jwtService.generateToken(
                refreshToken.getUser().getEmail()
        );

        return new AuthResponseDto(newAccessToken, requestToken);
    }

    @PostMapping("/logout")
    public String logout() {

        // Get logged in user's email from SecurityContext
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        refreshTokenService.deleteToken(email);

        return "Logged out successfully";
    }
}
