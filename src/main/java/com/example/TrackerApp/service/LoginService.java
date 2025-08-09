package com.example.TrackerApp.service;

import com.example.TrackerApp.dto.LoginRequest;
import com.example.TrackerApp.exception.DuplicateResourceException;
import com.example.TrackerApp.mapper.UserLoginMapper;
import com.example.TrackerApp.model.UserLogin;
import com.example.TrackerApp.repository.LoginRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
    @Autowired
    UserLoginMapper userLoginMapper;
    @Autowired
    LoginRepo loginRepo;
    @Autowired
    AuthenticationManager authManager;
    @Autowired
    JwtService jwtService;

    public void registration(LoginRequest loginRequest) {
        if(loginRepo.findByUsername(loginRequest.getUsername()).isPresent()){
            throw  new DuplicateResourceException("username already exists. please enter another one");
        }
        UserLogin userLogin = userLoginMapper.toUserLogin(loginRequest);
        loginRepo.save(userLogin);
    }

    public String logUser(LoginRequest loginRequest) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken
                        (loginRequest.getUsername(), loginRequest.getPassword()));
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        return jwtService.generateToken(userDetails.getUsername());
    }

}
