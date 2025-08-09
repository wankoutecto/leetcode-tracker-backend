package com.example.TrackerApp.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
public class Problem {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer pbId;
    private String title;
    private String link;
    private LocalDate dateSolved;
    private String note;
    @Column(columnDefinition = "TEXT")
    private String solutionCode;
    private Integer reviewLeft;
    @Embedded
    private ReviewStatus reviewStatus;
    @ManyToOne
    private UserLogin userLogin;
}
