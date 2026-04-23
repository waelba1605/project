package com.elearning.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuizDTO {
    private Long id;

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    private Integer passingScore;

    private Integer timeLimitMinutes;

    private Boolean isPublished;

    private Boolean showCorrectAnswers;

    @NotNull(message = "Lesson ID is required")
    private Long lessonId;

    private String lessonName;

    private Integer questionCount;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
