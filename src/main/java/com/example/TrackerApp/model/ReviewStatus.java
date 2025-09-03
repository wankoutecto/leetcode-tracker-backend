package com.example.TrackerApp.model;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.time.LocalDate;

@Embeddable
@Data
public class ReviewStatus {
    private LocalDate d3Date;
    private boolean d3Done;
    private LocalDate d7Date;
    private boolean d7Done;
    private LocalDate d14Date;
    private boolean d14Done;
    private LocalDate d28Date;
    private boolean d28Done;

    public void resetReviewStatus() {
        this.d3Done = false;
        this.d7Done = false;
        this.d14Done = false;
        this.d28Done = false;

        LocalDate solved = LocalDate.now();
        this.setD3Date(solved.plusDays(7));
        this.setD7Date(solved.plusDays(14));
        this.setD14Date(solved.plusDays(21));
        this.setD28Date(solved.plusDays(28));
    }
}
