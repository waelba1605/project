package com.elearning.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LessonProgressDTO {
    private Long id;
    private Long studentId;
    private String studentName;
    private Long lessonId;
    private String lessonTitle;
    private Boolean isCompleted;
    private Integer progressPercentage;
    private Integer watchedDurationMinutes;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
}
