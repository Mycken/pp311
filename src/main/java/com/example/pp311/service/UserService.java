package com.example.pp311.service;

import com.example.pp311.model.Role;
import com.example.pp311.model.User;
import java.util.List;


public interface UserService {
    void add(User user);
    void save(User user);
    List<User> getAllUsers();
    User getUserById(long id);
    void deleteById(long id);
    User getUserByUserName(String username);
    List<Role> getAllRoles();



}
