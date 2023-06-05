package com.example.api_spring.API.CONTROLER;
import com.example.api_spring.SERVICE.UserService;
import com.example.api_spring.API.MODEL.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.OptionalInt;

@RestController
public class UserControl {

    private UserService userservice;

    public UserControl(UserService userService){
        this.userservice = userService;
    }



    @GetMapping("/User")
    public User getUser(@RequestParam String cip){
        Optional user = userservice.getUser(cip);
        if(user.isPresent()){
            return (User) user.get();
        }
        return null;
    }
}
