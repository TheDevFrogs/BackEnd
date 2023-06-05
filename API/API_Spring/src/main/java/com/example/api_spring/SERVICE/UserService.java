package com.example.api_spring.SERVICE;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Optional;

import com.example.api_spring.API.MODEL.User;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    //Database of users
    private List<User> userlist;
    public UserService(){
        userlist = new ArrayList<>();
        User user1 = new User("R2P2", "Anakim", "Skywaker");
        User user2 = new User("ABC123", "Plak", "Dimat");
        User user3 = new User("bic399", "Jean", "Crayon");
        userlist.addAll(Arrays.asList(user1,user2,user3));
    }

    public Optional<User> getUser(String cip) {
        Optional optional = Optional.empty();
        for (User user : userlist){
            if(cip == user.getcip())
                optional = Optional.of(user);
                return optional;
        }
        return null;
    }
}
