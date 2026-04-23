package com.elearning.controller;

import com.elearning.dto.LessonProgressDTO;
import com.elearning.service.LessonProgressService;
import com.elearning.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/lesson-progress")
@CrossOrigin(origins = "*", maxAge = 3600)
public class LessonProgressController {

    @Autowired
    private LessonProgressService lessonProgressService;

    @PostMapping("/start")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> startLesson(@RequestParam Long studentId, @RequestParam Long lessonId) {
        try {
            LessonProgressDTO progress = lessonProgressService.startLesson(studentId, lessonId);
            return ResponseEntity.status(HttpStatus.CREATED).body(progress);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getLessonProgress(@PathVariable Long id) {
        try {
            LessonProgressDTO progress = lessonProgressService.getLessonProgress(id, id);
            return ResponseEntity.ok(progress);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping
    public ResponseEntity<List<LessonProgressDTO>> getAllProgress() {
        List<LessonProgressDTO> progress = lessonProgressService.getAllProgress();
        return ResponseEntity.ok(progress);
    }

    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
    public ResponseEntity<?> getStudentProgress(@PathVariable Long studentId) {
        try {
            List<LessonProgressDTO> progress = lessonProgressService.getStudentProgress(studentId);
            return ResponseEntity.ok(progress);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/lesson/{lessonId}")
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<?> getLessonStudentProgress(@PathVariable Long lessonId) {
        try {
            List<LessonProgressDTO> progress = lessonProgressService.getLessonProgress(lessonId);
            return ResponseEntity.ok(progress);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PutMapping("/{studentId}/{lessonId}/update")
    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
    public ResponseEntity<?> updateProgress(@PathVariable Long studentId, @PathVariable Long lessonId, @RequestParam Integer watchedMinutes) {
        try {
            LessonProgressDTO progress = lessonProgressService.updateLessonProgress(studentId, lessonId, watchedMinutes);
            return ResponseEntity.ok(progress);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PutMapping("/{studentId}/{lessonId}/complete")
    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
    public ResponseEntity<?> completeLesson(@PathVariable Long studentId, @PathVariable Long lessonId) {
        try {
            LessonProgressDTO progress = lessonProgressService.completeLesson(studentId, lessonId);
            return ResponseEntity.ok(progress);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
