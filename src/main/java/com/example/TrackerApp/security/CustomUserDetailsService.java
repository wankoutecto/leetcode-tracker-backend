package com.example.TrackerApp.security;

import com.example.TrackerApp.model.UserLogin;
import com.example.TrackerApp.repository.LoginRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    LoginRepo loginRepo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserLogin userLogin = loginRepo.findByUsername(username)
                .orElseThrow(()-> new RuntimeException("Username or password is incorrect"));
        return User.withUsername(userLogin.getUsername())
                .password(userLogin.getPassword())
                .roles("User")
                .build();
    }
}
