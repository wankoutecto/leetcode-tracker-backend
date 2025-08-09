package com.example.TrackerApp.controller;

import com.example.TrackerApp.dto.LoginRequest;
import com.example.TrackerApp.exception.DuplicateResourceException;
import com.example.TrackerApp.response.ApiResponse;
import com.example.TrackerApp.service.LoginService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class LoginController {
    @Autowired
    LoginService loginService;
    @PostMapping("/register")
    public ResponseEntity<?>registration(@Valid @RequestBody LoginRequest loginRequest,
                                                   BindingResult result){
        try {
            if (result.hasErrors()) {
                Map<String, String> errors = new HashMap<>();
                result.getFieldErrors().forEach(error ->
                        errors.put(error.getField(), error.getDefaultMessage())
                );
                return ResponseEntity.badRequest().body(errors);
            }
            loginService.registration(loginRequest);
            return ResponseEntity.ok(new ApiResponse(null, "User registered successfully"));
        } catch (DuplicateResourceException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());//Http 409 username already exists
        } catch (Exception e) {
            // Fallback for unexpected errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(null, "Something went wrong: " + e.getMessage()));
        }
    }
    @PostMapping("/login")
    public ResponseEntity<?>logUser(@RequestBody LoginRequest loginRequest){
        try {
            String token = loginService.logUser(loginRequest);
            HashMap<String, String> tokenMap = new HashMap<>();
            tokenMap.put("token", token);
            return ResponseEntity.ok(tokenMap);
        } catch (BadCredentialsException e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid username or password");
        }
    }
}
