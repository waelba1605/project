package com.elearning.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
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
    private Long instructorId;
    private String instructorName;
    private Boolean isPublished;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer lessonCount;
    private Integer enrollmentCount;
}
