package com.example.MyExpenseTracker.service;


import com.example.MyExpenseTracker.entity.RefreshToken;
import com.example.MyExpenseTracker.entity.User;
import com.example.MyExpenseTracker.repository.RefreshTokenRepository;
import com.example.MyExpenseTracker.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;


    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, UserRepository userRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }

    public RefreshToken generateRefreshToken(String email){

        User user = userRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException("User not found"));

        //delete the token if already exists
        refreshTokenRepository.deleteByUser(user);

        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setToken(UUID.randomUUID().toString()); // create a random string token
        refreshToken.setExpiryDate(Instant.now().plusSeconds(608400));
        refreshToken.setUser(user);

        return refreshTokenRepository.save(refreshToken);

    }


    public RefreshToken validateRefreshToken(String token){

        RefreshToken refreshtoken = refreshTokenRepository.findByToken(token).orElseThrow(() -> new NoSuchElementException("Invalid Token"));


        if(refreshtoken.getExpiryDate().isBefore(Instant.now())){
            refreshTokenRepository.delete(refreshtoken);
            throw new RuntimeException("Refresh token expired. Please login again.");

        }

        return refreshtoken;
    }

    @Transactional
    public void deleteToken(String email){

        User user = userRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException("User not found"));

        refreshTokenRepository.deleteByUser(user);
    }
}
