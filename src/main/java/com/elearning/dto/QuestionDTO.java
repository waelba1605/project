package com.elearning.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDTO {
    private Long id;

    @NotBlank(message = "Question text is required")
    private String questionText;

    private Integer questionNumber;

    private String questionType;

    private Integer points;

    @NotNull(message = "Quiz ID is required")
    private Long quizId;

    private Set<QuestionOptionDTO> options;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
