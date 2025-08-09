package com.example.TrackerApp.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class UserLogin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;
    private String username;
    private String password;
    @OneToMany(mappedBy = "userLogin", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Problem> problem;
}
