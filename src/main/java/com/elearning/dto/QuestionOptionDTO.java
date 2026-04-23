package com.elearning.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionOptionDTO {
    private Long id;
    private String optionText;
    private String optionLetter;
    private Boolean isCorrect;
    private Integer optionNumber;
    private Long questionId;
}
