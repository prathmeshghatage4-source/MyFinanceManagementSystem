package com.example.MyExpenseTracker.service;

import com.example.MyExpenseTracker.dto.LoginRequestDTO;
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

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JWTService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional
    public String verifyCredential(LoginRequestDTO dto) {

        User user = userRepository.findByEmail(dto.getEmail()).orElseThrow(() -> new RuntimeException("User Not found"));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid Credentials");
        }

        return jwtService.generateToken(user.getEmail());
    }
}
