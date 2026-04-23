package com.elearning.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentDTO {
    private Long id;

    @NotNull(message = "Student ID is required")
    private Long studentId;

    private String studentName;

    private String studentEmail;

    @NotNull(message = "Course ID is required")
    private Long courseId;

    private String courseName;

    private String enrollmentStatus;

    private Integer progressPercentage;

    private Boolean certificateEarned;

    private LocalDateTime enrolledAt;

    private LocalDateTime completedAt;
}
