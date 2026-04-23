package com.elearning.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuizRequest {
    @NotBlank
    private String title;

    private String description;

    @NotNull
    private Long lessonId;

    private Integer passingScore = 70;

    private Integer timeLimitMinutes;

    private Boolean showCorrectAnswers = true;
}
