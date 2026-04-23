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
public class LessonRequest {
    @NotBlank
    private String title;

    private String content;

    @NotNull
    private Integer lessonNumber;

    @NotNull
    private Long courseId;

    private String videoUrl;

    private Integer durationMinutes;
}
