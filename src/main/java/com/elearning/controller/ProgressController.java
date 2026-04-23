package com.elearning.controller;

import com.elearning.dto.LessonProgressDTO;
import com.elearning.service.ProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/progress")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ProgressController {

    @Autowired
    private ProgressService progressService;

    @PostMapping("/lesson/{lessonId}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> markLessonAsCompleted(@PathVariable Long lessonId) {
        try {
            LessonProgressDTO progressDTO = progressService.markLessonAsCompleted(lessonId);
            return ResponseEntity.status(HttpStatus.CREATED).body(progressDTO);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PutMapping("/lesson/{lessonId}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> updateLessonProgress(
            @PathVariable Long lessonId,
            @RequestParam Integer progressPercentage,
            @RequestParam(required = false) Integer watchedDuration) {
        try {
            LessonProgressDTO progressDTO = progressService.updateProgress(lessonId, progressPercentage, watchedDuration);
            return ResponseEntity.ok(progressDTO);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @GetMapping("/lesson/{lessonId}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> getLessonProgress(@PathVariable Long lessonId) {
        try {
            LessonProgressDTO progressDTO = progressService.getLessonProgress(lessonId);
            return ResponseEntity.ok(progressDTO);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/course/{courseId}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> getCourseProgress(@PathVariable Long courseId) {
        try {
            Map<String, Object> progress = progressService.getCourseProgress(courseId);
            return ResponseEntity.ok(progress);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
