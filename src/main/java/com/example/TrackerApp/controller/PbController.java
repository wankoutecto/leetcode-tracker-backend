package com.example.TrackerApp.controller;

import com.example.TrackerApp.dto.ProblemDto;
import com.example.TrackerApp.exception.DuplicateResourceException;
import com.example.TrackerApp.exception.ResourceAlreadyExistsException;
import com.example.TrackerApp.mapper.PbMapper;
import com.example.TrackerApp.model.Problem;
import com.example.TrackerApp.response.ApiResponse;
import com.example.TrackerApp.service.PbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("problem")
public class PbController {
    @Autowired
    PbService pbService;
    @Autowired
    PbMapper pbMapper;
    @PostMapping("add")
    public ResponseEntity<ApiResponse> addProblem(@RequestBody ProblemDto pbRequest){
        try {
            pbService.addUserProblem(pbRequest);
            return ResponseEntity.ok(new ApiResponse(null,pbRequest.getTitle()+ " added"));
        } catch (DuplicateResourceException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(null, e.getMessage()));
        } catch (Exception e) {
            // Fallback for unexpected errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(null, "Something went wrong: " + e.getMessage()));
        }
    }

    @GetMapping("due/today")
    public ResponseEntity<ApiResponse> getUserProblemDueToday(){
        try {
            return ResponseEntity.ok(new ApiResponse(pbService.getUserProblemDueToday()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(null, e.getMessage()));
        }
    }
    @GetMapping("completed")
    public ResponseEntity<ApiResponse> getUserProblemFullyReviewed(){
        try {
            return ResponseEntity.ok(new ApiResponse(pbService.getUserProblemFullyReviewed()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(null, e.getMessage()));
        }
    }

    @GetMapping("upcoming")
    public ResponseEntity<?> getUserProblemFutureReview(){
        try {
            return ResponseEntity.ok(new ApiResponse(pbService.getUserProblemFutureReview()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(null, e.getMessage()));
        }
    }

    @GetMapping("get/all")
    public ResponseEntity<?> getAllUserProblems(){
        try {
            return ResponseEntity.ok(new ApiResponse(pbService.getAllUserProblems()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(null, e.getMessage()));
        }
    }

    @GetMapping("get/{title}")
    public ResponseEntity<?> getUserProblem(@PathVariable String title){
        try {
            return ResponseEntity.ok(new ApiResponse(pbService.getUserProblemReview(title)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(null, e.getMessage()));
        }
    }


    @GetMapping("overdue")
    public ResponseEntity<?> getUserProblemOverdue(){
        try {
            return ResponseEntity.ok(new ApiResponse(pbService.getUserProblemOverdue()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(null, e.getMessage()));
        }
    }
    @GetMapping("home")
    public ResponseEntity<?> getFiveRandomProblem(){
        try {
            return ResponseEntity.ok(new ApiResponse(pbService.getFiveRandomProblem()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(null, e.getMessage()));
        }
    }

    @PostMapping("done/{title}")
    public ResponseEntity<?> markUserProblemDone(@PathVariable String title){
        try {
            pbService.markUserProblemDone(title);
            return ResponseEntity.ok(new ApiResponse(null, "Mark done successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(null, e.getMessage()));
        }
    }

    @PostMapping("update")
    public  ResponseEntity<?> updateProblem(@RequestBody ProblemDto problemDto){
        try {
            pbService.updateProblem(problemDto);
            return ResponseEntity.ok(new ApiResponse(null, "Successful update"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(null, e.getMessage()));
        }
    }

    @DeleteMapping("delete/{title}")
    public  ResponseEntity<?> deleteProblem(@PathVariable String title){
        try {
            pbService.deleteProblem(title);
            return ResponseEntity.ok(new ApiResponse(null, "Successful deletion"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(null, e.getMessage()));
        }
    }

    @PostMapping("addToDueToday/{title}")
    public ResponseEntity<ApiResponse> moveToDueToday(@PathVariable String title){
        try {
            pbService.moveToDueToday(title);
            return ResponseEntity.ok(new ApiResponse(null, "Add to Due Today"));
        } catch (ResourceAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(null, e.getMessage()));
        }
    }

    @PostMapping("reset/{title}")
    public ResponseEntity<?> resetProblem(@PathVariable String title){
        try {
            pbService.resetProblem(title);
            return ResponseEntity.ok(new ApiResponse(null, "Problem reset"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(null, e.getMessage()));
        }
    }

}
