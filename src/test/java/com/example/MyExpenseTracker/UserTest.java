package com.example.MyExpenseTracker;

import com.example.MyExpenseTracker.entity.User;
import com.example.MyExpenseTracker.repository.UserRepository;
import com.example.MyExpenseTracker.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private  UserService userService;



////    @Autowired
////    private final UserService userService;
//
//    @Test
//    public void transactionalUserUpdate(){
//
//        User user = userRepository.findById(1L).orElseThrow();
//
//        user.setEmail("newmail@gmail.com");
//
//        User updatedUser = userRepository.save(user);
//
//        System.out.println(updatedUser);
//    }


    @Test
    public void transactionalgetById(){

        System.out.println("user by this id is : " + userService.getUserById(1L) );
    }


}
