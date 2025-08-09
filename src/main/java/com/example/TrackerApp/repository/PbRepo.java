package com.example.TrackerApp.repository;


import com.example.TrackerApp.model.Problem;
import com.example.TrackerApp.model.UserLogin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PbRepo extends JpaRepository<Problem, Integer> {
    @Query("SELECT p FROM Problem p " +
            "WHERE p.userLogin.userId = :currentUserId " +
            "AND (" +
            "p.reviewStatus.d3Date = :today AND p.reviewStatus.d3Done = false OR " +
            "p.reviewStatus.d7Date = :today AND p.reviewStatus.d7Done = false OR " +
            "p.reviewStatus.d14Date = :today AND p.reviewStatus.d14Done = false OR " +
            "p.reviewStatus.d28Date = :today AND p.reviewStatus.d28Done = false) " +
            "ORDER BY p.pbId")
    List<Problem> findAllByUserAndDueToday(@Param("currentUserId")Integer currentUserId, @Param("today")LocalDate today);
    @Query("SELECT p FROM Problem p " +
            "WHERE p.userLogin.userId = :currentUserId " +
            "AND (" +
            "p.reviewStatus.d3Date < :today AND p.reviewStatus.d3Done = false OR " +
            "p.reviewStatus.d7Date < :today AND p.reviewStatus.d7Done = false OR " +
            "p.reviewStatus.d14Date < :today AND p.reviewStatus.d14Done = false OR " +
            "p.reviewStatus.d28Date < :today AND p.reviewStatus.d28Done = false) " +
            "ORDER BY p.pbId")
    List<Problem> findAllByUserAndOverdue(@Param("currentUserId")Integer currentUserId, @Param("today")LocalDate today);
    @Query("SELECT p FROM Problem p " +
            "WHERE p.userLogin.userId = :currentUserId " +
            "AND (" +
            "p.reviewStatus.d3Done = true AND " +
            "p.reviewStatus.d7Done = true AND " +
            "p.reviewStatus.d14Done = true AND " +
            "p.reviewStatus.d28Done = true) " +
            "ORDER BY p.pbId")
    List<Problem> findAllByUserAndFullyReviewed(@Param("currentUserId")Integer currentUserId, @Param("today")LocalDate today);

    Optional<Problem> findByTitleAndUserLogin_UserId(String title, Integer userId);

    List<Problem> findAllByUserLogin_UserId(Integer userId);
    @Query("SELECT p FROM Problem p " +
            "WHERE p.userLogin.userId = :currentUserId " +
            "AND (" +
            "(p.reviewStatus.d3Date > :today AND p.reviewStatus.d3Done = false) OR " +
            "(p.reviewStatus.d7Date > :today AND p.reviewStatus.d7Done = false) OR " +
            "(p.reviewStatus.d14Date > :today AND p.reviewStatus.d14Done = false) OR " +
            "(p.reviewStatus.d28Date > :today AND p.reviewStatus.d28Done = false)) " +
            "ORDER BY p.pbId")
    List<Problem> findAllByUserAndFutureReview(@Param("currentUserId")Integer currentUserId, @Param("today")LocalDate today);
    @Query(value = "SELECT * FROM " +
                   "(SELECT DISTINCT ON (title) * FROM problem ORDER BY title, RANDOM()) " +
                   "SUB ORDER BY RANDOM() LIMIT 5", nativeQuery = true)
    List<Problem> findFiveRandomProblem();
}
