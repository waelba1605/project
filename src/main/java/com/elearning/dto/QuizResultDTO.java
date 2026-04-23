package com.elearning.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuizResultDTO {
    private Long id;
    private Long studentId;
    private String studentName;
    private Long quizId;
    private String quizTitle;
    private Integer score;
    private Integer totalPoints;
    private Double percentageScore;
    private Boolean isPassed;
    private Integer attemptNumber;
    private LocalDateTime completedAt;
    private LocalDateTime createdAt;
}
