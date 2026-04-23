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
public class LessonDTO {
    private Long id;

    @NotBlank(message = "Title is required")
    private String title;

    private String content;

    private Integer lessonNumber;

    private String videoUrl;

    private Integer durationMinutes;

    private Boolean isPublished;

    @NotNull(message = "Course ID is required")
    private Long courseId;

    private String courseName;

    private Integer quizCount;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
