package com.elearning.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QuizDTO {
    private Long id;

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @NotNull(message = "Lesson ID is required")
    private Long lessonId;

    private Integer passingScore;
    private Integer timeLimitMinutes;
    private Boolean isPublished;
    private Boolean showCorrectAnswers;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
