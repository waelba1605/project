package com.elearning.controller;

import com.elearning.dto.ProgressDTO;
import com.elearning.service.ProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/progress")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ProgressController {

    @Autowired
    private ProgressService progressService;

    @PostMapping
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ProgressDTO> updateProgress(@RequestBody ProgressDTO progressDTO) {
        ProgressDTO updated = progressService.updateLessonProgress(progressDTO);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/lesson/{lessonId}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ProgressDTO> getLessonProgress(@PathVariable Long lessonId) {
        ProgressDTO progress = progressService.getLessonProgress(lessonId);
        return ResponseEntity.ok(progress);
    }

    @GetMapping("/course/{courseId}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<ProgressDTO>> getCourseProgress(@PathVariable Long courseId) {
        List<ProgressDTO> progress = progressService.getCourseProgress(courseId);
        return ResponseEntity.ok(progress);
    }

    @GetMapping("/enrollment/{enrollmentId}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<Integer> getEnrollmentProgress(@PathVariable Long enrollmentId) {
        Integer progress = progressService.calculateEnrollmentProgress(enrollmentId);
        return ResponseEntity.ok(progress);
    }
}
