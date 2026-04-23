package com.elearning.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionOptionDTO {
    private Long id;

    @NotBlank(message = "Option text is required")
    private String optionText;

    private String optionLetter;

    private Boolean isCorrect;

    private Integer optionNumber;

    private Long questionId;
}
