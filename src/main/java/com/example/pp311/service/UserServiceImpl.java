package com.example.pp311.service;

import com.example.pp311.model.Role;
import com.example.pp311.model.User;
import com.example.pp311.repository.RoleRepository;
import com.example.pp311.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    @Autowired
    public UserServiceImpl(UserRepository userDao, RoleRepository roleRepository) {
        this.userRepository = userDao;
        this.roleRepository = roleRepository;
    }

    @Transactional
    @Override
    public void add(User user) {
        userRepository.save(user);
    }

    @Transactional
    @Override
    public void save(User user) { userRepository.save(user); }

    @Override
    @Transactional
    public void deleteById(long id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public List<Role> getAllRoles(){return  roleRepository.findAll();}

    @Override
    public User getUserById(long id) {
        Optional<User> optionalUser=userRepository.findById(id);
        return optionalUser.get();
    }

    @Override
    public User getUserByUserName(String username) {
        List<User> users = userRepository.findAll();
        Optional<User> user = users.stream()
                .filter(u -> u.getUsername().equals(username))
                .findAny();
        return user.get();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<User> users = userRepository.findAll();
        Optional<User> user = users.stream()
                .filter(u -> u.getUsername().equals(username))
                .findAny();
        if (!user.isPresent()) {
            throw new UsernameNotFoundException("User not found by name: " + username);
        }
        return User.fromUser(user.get());
    }
}
