package com.example.TrackerApp.response;

import lombok.AllArgsConstructor;
import lombok.Data;
@Data
public class ApiResponse {
    private Object data;
    private String message;

    public ApiResponse(Object data) {
        this.data = data;
    }

    public ApiResponse(Object data, String message) {
        this.data = data;
        this.message = message;
    }
}

