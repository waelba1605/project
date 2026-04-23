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
public class CourseDTO {
    private Long id;

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @NotBlank(message = "Course code is required")
    private String courseCode;

    private String category;

    private String level;

    private String thumbnailUrl;

    private Integer durationHours;

    private Double price;

    @NotNull(message = "Instructor ID is required")
    private Long instructorId;

    private String instructorName;

    private Boolean isPublished;

    private Integer lessonCount;

    private Integer enrollmentCount;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
