package com.elearning.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuizResultRequest {
    @NotNull
    private Long quizId;

    @NotNull
    private Integer score;

    @NotNull
    private Integer totalPoints;
}
