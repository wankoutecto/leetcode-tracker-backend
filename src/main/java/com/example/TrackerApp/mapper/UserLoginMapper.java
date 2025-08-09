package com.example.TrackerApp.mapper;

import com.example.TrackerApp.dto.LoginRequest;
import com.example.TrackerApp.model.UserLogin;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserLoginMapper {
    private final BCryptPasswordEncoder passwordEncoder;

    public UserLoginMapper(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public UserLogin toUserLogin(LoginRequest loginRequest){
        UserLogin userLogin = new UserLogin();
        userLogin.setUsername(loginRequest.getUsername());
        String hashPassword  = passwordEncoder.encode(loginRequest.getPassword());
        userLogin.setPassword(hashPassword);
        return userLogin;
    }
}
