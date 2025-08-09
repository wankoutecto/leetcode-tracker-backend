package com.example.TrackerApp.repository;

import com.example.TrackerApp.model.UserLogin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoginRepo extends JpaRepository<UserLogin, Integer> {
    Optional<UserLogin> findByUsername(String username);
}
