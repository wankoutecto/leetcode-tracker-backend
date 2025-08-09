package com.example.TrackerApp.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ProblemDto {
    private String title;
    private String link;
    private LocalDate dateSolved;
    private String note;
    private String solutionCode;
    private Integer reviewLeft;
}
