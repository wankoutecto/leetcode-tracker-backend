package com.example.TrackerApp.service;

import com.example.TrackerApp.dto.ProblemDto;
import com.example.TrackerApp.exception.DuplicateResourceException;
import com.example.TrackerApp.exception.ResourceAlreadyExistsException;
import com.example.TrackerApp.mapper.PbMapper;
import com.example.TrackerApp.model.Problem;
import com.example.TrackerApp.model.ReviewStatus;
import com.example.TrackerApp.model.UserLogin;
import com.example.TrackerApp.repository.LoginRepo;
import com.example.TrackerApp.repository.PbRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class PbService {
    @Autowired
    PbRepo pbRepo;
    @Autowired
    PbMapper pbMapper;
    @Autowired
    LoginRepo loginRepo;

    public void addUserProblem(ProblemDto pbRequest) {
        UserLogin currentUser = getCurrentUser();
        if(pbRepo.findByTitleAndUserLogin_UserId(pbRequest.getTitle(), currentUser.getUserId()).isPresent()){
            throw new DuplicateResourceException("You have already added this LeetCode problem");
        }
        Problem pb = pbMapper.toProblem(pbRequest, currentUser);
        pbRepo.save(pb);
    }
    //helper function
    public UserLogin getCurrentUser(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return loginRepo.findByUsername(username)
                .orElseThrow(()-> new RuntimeException("User is not logged in"));
    }


    public List<ProblemDto> getUserProblemDueToday() {
        Integer currentUserId = getCurrentUser().getUserId();
        LocalDate today = LocalDate.now();
        List<Problem> problemList = pbRepo.findAllByUserAndDueToday(currentUserId, today);
        return pbMapper.toProblemDtoList(problemList);
    }
    public List<ProblemDto>getUserProblemOverdue() {
        Integer currentUserId = getCurrentUser().getUserId();
        LocalDate today = LocalDate.now();
        List<Problem> problemList = pbRepo.findAllByUserAndOverdue(currentUserId, today);
        return pbMapper.toProblemDtoList(problemList);
    }

    public List<ProblemDto> getUserProblemFullyReviewed(){
        Integer currentUserId = getCurrentUser().getUserId();
        LocalDate today = LocalDate.now();
        List<Problem> problemList = pbRepo.findAllByUserAndFullyReviewed(currentUserId, today);
        return pbMapper.toProblemDtoList(problemList);
    }

    public List<ProblemDto> getUserProblemFutureReview() {
        Integer currentUserId = getCurrentUser().getUserId();
        LocalDate today = LocalDate.now();
        List<Problem> problemList = pbRepo.findAllByUserAndFutureReview(currentUserId, today);
        return pbMapper.toProblemDtoList(problemList);
    }
    public List<ProblemDto> getAllUserProblems(){
        Integer currentUserId = getCurrentUser().getUserId();
        List<Problem> problemList = pbRepo.findAllByUserLogin_UserId(currentUserId);
        return pbMapper.toProblemDtoList(problemList);
    }

    public ProblemDto getUserProblemReview(String title) {
        Problem problem = getProblemByTitle(title);
        return pbMapper.toProblemDto(problem);
    }
    //helper function
    public Problem getProblemByTitle(String title) {
        Integer currentUserId = getCurrentUser().getUserId();
        return pbRepo.findByTitleAndUserLogin_UserId(title, currentUserId)
                .orElseThrow(()->new RuntimeException("Problem not found"));
    }

    public void markUserProblemDone(String title) {
        LocalDate today = LocalDate.now();
        Problem problem = getProblemByTitle(title);
        ReviewStatus status = problem.getReviewStatus();
        boolean updated = false;
        if(status.getD3Date().equals(today) && !status.isD3Done()){
            status.setD3Done(true);
            updated =true;
        } else if(status.getD7Date().equals(today) && !status.isD7Done()){
            status.setD7Done(true);
            updated =true;
        } else if (status.getD14Date().equals(today) && !status.isD14Done()) {
            status.setD14Done(true);
            updated =true;
        } else if (status.getD28Date().equals(today) && !status.isD28Done()) {
            status.setD28Done(true);
            updated =true;
        }
        if(!updated){
            throw new IllegalArgumentException("You can't review the problem today or it is fully reviewed");
        }
        problem.setReviewLeft(problem.getReviewLeft() - 1);
        pbRepo.save(problem);
    }

    public void moveToDueToday(String title){
        UserLogin currentUser = getCurrentUser();
        Problem problem = getProblemByTitle(title);
        List<ProblemDto> problemDtoList = getUserProblemDueToday();

        boolean titleExist = problemDtoList.stream()
                .anyMatch(problemDto ->
                        problemDto.getTitle().equals(problem.getTitle()));
        if(titleExist){
            throw new ResourceAlreadyExistsException("Can't move. Problem is already on Due Today List");
        }

        LocalDate today = LocalDate.now();
        boolean updated = false;
        ReviewStatus reviewStatus = problem.getReviewStatus();
        if(reviewStatus.getD3Date().isAfter(today) && !reviewStatus.isD3Done()){
            reviewStatus.setD3Date(today);
            updated = true;
        }else if(reviewStatus.getD7Date().isAfter(today) && !reviewStatus.isD7Done()){
            reviewStatus.setD7Date(today);
            updated = true;
        } else if (reviewStatus.getD14Date().isAfter(today) && !reviewStatus.isD14Done()) {
            reviewStatus.setD14Date(today);
            updated = true;
        } else if (reviewStatus.getD28Date().isAfter(today) && !reviewStatus.isD28Done()) {
            reviewStatus.setD28Date(today);
            updated = true;
        }

        if(!updated){
            throw new IllegalArgumentException
                    ("You can't move problem to today list.");
        }
        pbRepo.save(problem);
    }

    public List<ProblemDto> getFiveRandomProblem() {
        List<Problem> problemList = pbRepo.findFiveRandomProblem();
        return pbMapper.toProblemDtoList(problemList);
    }

    public void updateProblem(ProblemDto problemDto) {
        UserLogin currentUser = getCurrentUser();
        Problem problem = getProblemByTitle(problemDto.getTitle());
        problem.setNote(problemDto.getNote());
        problem.setSolutionCode(problemDto.getSolutionCode());
        pbRepo.save(problem);
    }

    public void deleteProblem(String title) {
        UserLogin currentUser = getCurrentUser();
        Problem problem = getProblemByTitle(title);
        pbRepo.delete(problem);
    }

    public void resetProblem(String title) {
        Problem problem = getProblemByTitle(title);
        problem.getReviewStatus().resetReviewStatus();
        Integer numberReview = pbMapper.findNumberReview(problem);
        problem.setReviewLeft(numberReview);
        pbRepo.save(problem);
    }
}
