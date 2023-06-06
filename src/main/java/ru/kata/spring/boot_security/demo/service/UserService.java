package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserService extends UserDetailsService {

    List<User> getUsers();

    void saveUsers(User user);

    User getUser(int ID);

    void updateUser(User user);

    void deleteUser(int ID);

    User getUserByEmail(String email);

    User getUserByName(String name);
}
