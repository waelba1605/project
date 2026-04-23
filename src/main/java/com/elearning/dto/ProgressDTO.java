package com.elearning.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProgressDTO {
    private Long id;
    private Long studentId;
    private Long lessonId;
    private String lessonTitle;
    private Boolean isCompleted;
    private Integer progressPercentage;
    private Integer watchedDurationMinutes;
    private Integer totalDurationMinutes;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
}
