package com.example.MyExpenseTracker.service;

import com.example.MyExpenseTracker.dto.AuthResponseDto;
import com.example.MyExpenseTracker.dto.LoginRequestDTO;
import com.example.MyExpenseTracker.entity.RefreshToken;
import com.example.MyExpenseTracker.entity.User;
import com.example.MyExpenseTracker.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final RefreshTokenService refreshTokenService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JWTService jwtService, RefreshTokenService refreshTokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
    }

    @Transactional
    public AuthResponseDto verifyCredential(LoginRequestDTO dto) {

        User user = userRepository.findByEmail(dto.getEmail()).orElseThrow(() -> new RuntimeException("User Not found"));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid Credentials");
        }

        String accessToken = jwtService.generateToken(user.getEmail());
        RefreshToken refreshToken = refreshTokenService.generateRefreshToken(user.getEmail());

//        return jwtService.generateToken(user.getEmail());
        return new AuthResponseDto(accessToken, refreshToken.getToken());
    }
}
