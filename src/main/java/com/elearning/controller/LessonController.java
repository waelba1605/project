package com.elearning.controller;

import com.elearning.dto.LessonDTO;
import com.elearning.service.LessonService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lessons")
@CrossOrigin(origins = "*", maxAge = 3600)
public class LessonController {

    @Autowired
    private LessonService lessonService;

    @PostMapping
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<LessonDTO> createLesson(@Valid @RequestBody LessonDTO lessonDTO) {
        LessonDTO createdLesson = lessonService.createLesson(lessonDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdLesson);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LessonDTO> getLessonById(@PathVariable Long id) {
        LessonDTO lesson = lessonService.getLessonById(id);
        return ResponseEntity.ok(lesson);
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<LessonDTO>> getLessonsByCourse(@PathVariable Long courseId) {
        List<LessonDTO> lessons = lessonService.getLessonsByCourse(courseId);
        return ResponseEntity.ok(lessons);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<LessonDTO> updateLesson(
            @PathVariable Long id,
            @Valid @RequestBody LessonDTO lessonDTO) {
        LessonDTO updatedLesson = lessonService.updateLesson(id, lessonDTO);
        return ResponseEntity.ok(updatedLesson);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<?> deleteLesson(@PathVariable Long id) {
        lessonService.deleteLesson(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/publish")
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<LessonDTO> publishLesson(@PathVariable Long id) {
        LessonDTO lesson = lessonService.publishLesson(id);
        return ResponseEntity.ok(lesson);
    }
}
