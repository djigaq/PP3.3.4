package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repositories.UserRepositories;


import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserServiceImp implements UserService {

    private final UserRepositories userRepositories;

    @Autowired
    public UserServiceImp(UserRepositories userRepositories) {
        this.userRepositories = userRepositories;
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> getUsers() {
        return userRepositories.findAll();
    }

    @Transactional
    @Override
    public void saveUsers(User user) {
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userRepositories.save(user);
    }

    @Transactional(readOnly = true)
    @Override
    public User getUser(int ID) {
        return userRepositories.getById(ID);
    }

    @Transactional
    @Override
    public void updateUser(User user) {
        userRepositories.save(user);
    }

    @Transactional
    @Override
    public void deleteUser(int id) {
        userRepositories.deleteById(id);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepositories.getUserByEmail(email);
    }

    @Override
    public User getUserByName(String name) {
        return userRepositories.getUserByName(name);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = getUserByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("User '%s' not found", email));
        }

        return new org.springframework.security.core.userdetails.User(user.getName(), user.getPassword(), mapRolesToAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream().map(u -> new SimpleGrantedAuthority(u.getRoleName())).collect(Collectors.toList());
    }
}

