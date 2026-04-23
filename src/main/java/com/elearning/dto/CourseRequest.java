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
public class CourseRequest {
    @NotBlank
    private String title;

    private String description;

    @NotBlank
    private String courseCode;

    private String category;

    private String level;

    private String thumbnailUrl;

    private Integer durationHours;

    private Double price;
}
