package com.elearning.controller;

import com.elearning.dto.QuizDTO;
import com.elearning.service.QuizService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/quizzes")
@CrossOrigin(origins = "*", maxAge = 3600)
public class QuizController {

    @Autowired
    private QuizService quizService;

    @PostMapping
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<?> createQuiz(@Valid @RequestBody QuizDTO quizDTO) {
        try {
            QuizDTO createdQuiz = quizService.createQuiz(quizDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdQuiz);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getQuiz(@PathVariable Long id) {
        try {
            QuizDTO quiz = quizService.getQuizById(id);
            return ResponseEntity.ok(quiz);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping
    public ResponseEntity<List<QuizDTO>> getAllQuizzes() {
        List<QuizDTO> quizzes = quizService.getAllQuizzes();
        return ResponseEntity.ok(quizzes);
    }

    @GetMapping("/lesson/{lessonId}")
    public ResponseEntity<?> getQuizzesByLesson(@PathVariable Long lessonId) {
        try {
            List<QuizDTO> quizzes = quizService.getQuizzesByLesson(lessonId);
            return ResponseEntity.ok(quizzes);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<?> updateQuiz(@PathVariable Long id, @Valid @RequestBody QuizDTO quizDTO) {
        try {
            QuizDTO updatedQuiz = quizService.updateQuiz(id, quizDTO);
            return ResponseEntity.ok(updatedQuiz);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PutMapping("/{id}/publish")
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<?> publishQuiz(@PathVariable Long id) {
        try {
            quizService.publishQuiz(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Quiz published successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PutMapping("/{id}/unpublish")
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<?> unpublishQuiz(@PathVariable Long id) {
        try {
            quizService.unpublishQuiz(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Quiz unpublished successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<?> deleteQuiz(@PathVariable Long id) {
        try {
            quizService.deleteQuiz(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Quiz deleted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}
