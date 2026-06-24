package com.example.MyExpenseTracker.controller;


import com.example.MyExpenseTracker.dto.UserRequestDTO;
import com.example.MyExpenseTracker.dto.UserResponseDTO;
import com.example.MyExpenseTracker.entity.User;
import com.example.MyExpenseTracker.repository.UserRepository;
import com.example.MyExpenseTracker.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")

@Tag(name = "User APIs", description = "Operations related to users")

public class UserController {


    private final UserRepository userRepository;

    private final UserService userService;

    public UserController(UserRepository userRepository, UserService userService) { this.userRepository = userRepository;
        this.userService = userService;
    }

    @GetMapping("")
    public Page<User> getAllUser(@RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "10") int size){
        return userService.getAllUser(page,size);
    }

    @GetMapping("/{id}")
    @Operation(summary = "get user by Id")
    public User getUserById(@PathVariable long id){
        return userService.getUserById(id);
    }


    @PostMapping
    @Operation(summary = "Adding of new User")
    public UserResponseDTO addUser(@Valid @RequestBody UserRequestDTO user) {



//        for(MyExpense expense : user.getMyExpense()){
//            expense.setUser(user);
//        }
        return userService.addUser(user);

    }

    @DeleteMapping("/{id}")
    public String  deleteUserById(@PathVariable long id){
       return  userService.deleteUserById(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Updation of existing  Expense")
    public User updateUserByID(@PathVariable Long id, @RequestBody User updateUser){
         return userService.UserUpdateById(id, updateUser);

    }

}
