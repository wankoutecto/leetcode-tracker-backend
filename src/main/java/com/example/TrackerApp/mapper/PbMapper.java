package com.example.TrackerApp.mapper;

import com.example.TrackerApp.dto.ProblemDto;
import com.example.TrackerApp.model.Problem;
import com.example.TrackerApp.model.ReviewStatus;
import com.example.TrackerApp.model.UserLogin;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class PbMapper {
    public Problem toProblem(ProblemDto pbRequest, UserLogin userLogin){
        Problem pb = new Problem();
        pb.setTitle(pbRequest.getTitle());
        pb.setLink(pbRequest.getLink());
        pb.setDateSolved(pbRequest.getDateSolved());
        pb.setNote(pbRequest.getNote());
        pb.setSolutionCode(pbRequest.getSolutionCode());
        ReviewStatus reviewStatus = new ReviewStatus();
        reviewStatus.resetReviewStatus();
        pb.setReviewStatus(reviewStatus);
        Integer number = findNumberReview(pb);
        pb.setReviewLeft(number);

        pb.setUserLogin(userLogin);
        return pb;
    }

    public ProblemDto toProblemDto(Problem problem) {
        ProblemDto dto = new ProblemDto();
        dto.setTitle(problem.getTitle());
        dto.setLink(problem.getLink());
        dto.setDateSolved(problem.getDateSolved());
        dto.setNote(problem.getNote());
        dto.setSolutionCode(problem.getSolutionCode());
        dto.setReviewLeft(problem.getReviewLeft());
        return dto;
    }

    public List<ProblemDto> toProblemDtoList(List<Problem> problems){
        return problems.stream()
                .map(this::toProblemDto)
                .collect(Collectors.toList());
    }
    //helper function
    public Integer findNumberReview(Problem pb){
        List<Problem> problemList = List.of(pb);
        Integer number = problemList
                .stream()
                .map(Problem::getReviewStatus)
                .flatMap(r -> Stream.of(
                        r.isD3Done(),
                        r.isD7Done(),
                        r.isD14Done(),
                        r.isD28Done()))
                .filter(b->!b)
                .mapToInt(b ->1)
                .sum();
        return number;
    }

}
