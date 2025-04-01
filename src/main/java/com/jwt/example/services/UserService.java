package com.jwt.example.services;

import com.jwt.example.models.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    private List<User> store = new ArrayList<>();

    public UserService() {
        store.add(new User(UUID.randomUUID().toString(),"Shruti Mahale","mahaleshruti@gmail.com"));
        store.add(new User(UUID.randomUUID().toString(),"Tanush Mahale","mahaletanush@gmail.com"));
        store.add(new User(UUID.randomUUID().toString(),"Deepali Mahale","mahaledeepali@gmail.com"));
        store.add(new User(UUID.randomUUID().toString(),"Nilesh Mahale","mahalenilesh@gmail.com"));
    }

    public List<User> getUsers() {
        return this.store;
    }
}
