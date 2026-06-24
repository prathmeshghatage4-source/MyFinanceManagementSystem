package com.example.MyExpenseTracker.service;

import com.example.MyExpenseTracker.dto.UserRequestDTO;
import com.example.MyExpenseTracker.dto.UserResponseDTO;
import com.example.MyExpenseTracker.entity.User;
import com.example.MyExpenseTracker.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.NoSuchElementException;

@Service

public class UserService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final PasswordEncoder passwordEncoder;


    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
            }

    @Transactional
    public Page<User> getAllUser(int page , int size){
//        List<User> userList = userRepository.findAll();
        Pageable pageable = PageRequest.of( page, size);

        return userRepository.findAll(pageable);
    }

    @Transactional
    public User getUserById(long id){
        User user = userRepository.findById(id).orElseThrow(() -> new NoSuchElementException("User with id " + id + " not found"));

        System.out.println("This is the information of the user with id " + id + " : " + user);
        return user;
    }

    @Transactional
    public User UserUpdateById( Long id,  User userUpdate){

        User user = userRepository.findById(id).orElseThrow(() -> new NoSuchElementException("User with id " + id + " not found"));
        user.setName(userUpdate.getName());
        user.setEmail(userUpdate.getEmail());
        user.setRole(userUpdate.getRole());


        return userRepository.save(user);
    }

    @Transactional
    public UserResponseDTO addUser(UserRequestDTO dto){

        // DTO -> Entity
        User user = new User();

        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(dto.getRole());

        // Save entity
        User savedUser = userRepository.save(user);

        // Entity -> ResponseDTO
        UserResponseDTO response = new UserResponseDTO();

        response.setId(savedUser.getId());
        response.setName(savedUser.getName());
        response.setEmail(savedUser.getEmail());
        response.setRole(savedUser.getRole());

        return response;
    }

    @Transactional
    public String  deleteUserById( long id){
        userRepository.deleteById(id);

        return " Id Deleted " + id ;
    }
}
