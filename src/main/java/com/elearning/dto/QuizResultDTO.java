package com.elearning.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
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
