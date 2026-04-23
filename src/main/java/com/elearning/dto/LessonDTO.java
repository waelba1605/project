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
public class LessonDTO {
    private Long id;

    @NotBlank(message = "Title is required")
    private String title;

    private String content;

    @NotNull(message = "Course ID is required")
    private Long courseId;

    private Integer lessonNumber;
    private String videoUrl;
    private Integer durationMinutes;
    private Boolean isPublished;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
